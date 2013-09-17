/*
 * Copyright (C) 2013 ENTERTAILION, LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.entertailion.java.chromecastpi;

import java.net.InetAddress;

import com.entertailion.java.caster.DeviceFinder;
import com.entertailion.java.caster.DeviceFinderListener;
import com.entertailion.java.caster.DialServer;
import com.entertailion.java.caster.Log;
import com.entertailion.java.caster.Platform;
import com.entertailion.java.caster.Playback;
import com.entertailion.java.caster.PlaybackListener;
import com.entertailion.java.caster.TrackedDialServers;

/**
 * Main class for running the YouTube queueing app. Todays most popular YouTube
 * videos are queued on a ChromeCast device.
 * 
 * Uses Caster library: https://github.com/entertailion/Caster
 * 
 * @see https://github.com/entertailion/ChromeCastPi
 * 
 * @author leon_nicholls
 * 
 */
public class Main {

	private static final String LOG_TAG = "Main";

	private static Platform platform = new Platform();

	private static int counter;

	private static Downloader downloader;

	/**
	 * @param args
	 *            first argument is the ChromeCast device IP address
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Error: Missing ChromeCast device IP address command-line argument.\nUsage: java -jar chromecastpi.jar 192.168.0.22");
			return;
		}
		// Debugging
		Log.setVerbose(true);
		com.entertailion.java.caster.Log.setVerbose(true);

		// Invoke the YouTube API for today's most popular videos
		downloader = new Downloader();
		downloader.getFeed();

		// Print the YouTube video Id's
		for (String video : downloader.getVideos()) {
			Log.d(LOG_TAG, video);
		}

		try {
			// Find the ChromeCast devices
			final DeviceFinder deviceFinder = new DeviceFinder(new DeviceFinderListener() {

				@Override
				public void discoveringDevices(DeviceFinder deviceFinder) {
					Log.d(LOG_TAG, "discoveringDevices");
				}

				@Override
				public void discoveredDevices(DeviceFinder deviceFinder) {
					Log.d(LOG_TAG, "discoveredDevices");
					TrackedDialServers trackedDialServers = deviceFinder.getTrackedDialServers();
					for (DialServer dialServer : trackedDialServers) {
						Log.d(LOG_TAG, dialServer.toString());
					}
				}

			});
			deviceFinder.discoverDevices();

			// wait for devices to be found
			Thread.sleep(10000);

			counter = 0;
			DialServer dialServer = deviceFinder.getTrackedDialServers().findDialServer(InetAddress.getByName(args[0]));
			if (dialServer != null) {
				// Launch the YouTube app
				Playback playback = new Playback(platform, "YouTube", dialServer, new PlaybackListener() {

					private int time;
					private int duration;
					private int state;

					@Override
					public void updateTime(Playback playback, int time) {
						Log.d(LOG_TAG, "updateTime: " + time);
						this.time = time;
					}

					@Override
					public void updateDuration(Playback playback, int duration) {
						Log.d(LOG_TAG, "updateDuration: " + duration);
						this.duration = duration;
					}

					@Override
					public void updateState(Playback playback, int state) {
						Log.d(LOG_TAG, "updateState: " + state);
						this.state = state;
						// Stop the app if the video reaches the end
						if (this.time > 0 && (this.time == this.duration || state == 1)) {
							playback.doStop();
							counter++;
							if (counter < downloader.getVideos().size()) {
								// Play the next video
								playback.launch("v=" + downloader.getVideos().get(counter));
							}
						}
					}

				});
				// Give the YouTube app the YouTube video id
				// e.g. http://www.youtube.com/watch?v=cKG5HDyTW8o
				playback.launch("v=" + downloader.getVideos().get(counter));
				// Pause the first video
				playback.doPause();
			} else {
				Log.e(LOG_TAG, "ChromeCast device not found: " + args[0]);
			}
		} catch (Exception e1) {
			Log.e(LOG_TAG, "main", e1);
		}
	}

}
