/*
 * Copyright (C) 2013 ENTERTAILION LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entertailion.java.chromecastpi;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaPlayer;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.data.youtube.YouTubeMediaContent;
import com.google.gdata.data.youtube.YouTubeMediaGroup;

/**
 * Utility class to invoke the YouTube API for a video feed.
 * 
 * @author leon_nicholls
 *
 */
public class Downloader {
	private static final String LOG_TAG = "Downloader";

	// TODO Change to your YouTube API developer key
	private static final String YOUTUBE_API_DEVELOPER_KEY = "YOUR_YOUTUBE_API_DEVELOPER_KEY";

	// https://developers.google.com/youtube/2.0/developers_guide_protocol_video_feeds
	private static final String MOST_POPULAR = "https://gdata.youtube.com/feeds/api/standardfeeds/most_popular";
	// https://developers.google.com/youtube/2.0/developers_guide_protocol_api_query_parameters#timesp
	private static final String TODAY = "?time=today";

	private ArrayList<String> videos = new ArrayList<String>();

	/**
	 * Invoke the YouTube API and retrieve the YouTube Id's of the most popular videos for today
	 */
	public void getFeed() {
		Log.d(LOG_TAG, "getFeed");
		try {
			YouTubeService youTubeService = new YouTubeService("ChromeCastPi", YOUTUBE_API_DEVELOPER_KEY);

			VideoFeed videoFeed = youTubeService.getFeed(new URL(MOST_POPULAR + TODAY), VideoFeed.class);
			String title = videoFeed.getTitle().getPlainText();
			Log.d(LOG_TAG, "title=" + title);

			List<VideoEntry> videoEntries = videoFeed.getEntries();
			if (videoEntries.size() == 0) {
				Log.d(LOG_TAG, "This feed contains no entries.");
				return;
			}
			int count = 1;
			String[] ids;
			videos.clear();
			for (VideoEntry videoEntry : videoEntries) {
				ids = videoEntry.getId().split(":"); // tag:youtube.com,2008:video:Bbgz4yY-xX0
				Log.d(LOG_TAG, "(Video id: " + ids[ids.length - 1] + ")");
				if (!videos.contains(ids[ids.length - 1])) {
					videos.add(ids[ids.length - 1]);
				}
				Log.d(LOG_TAG, "(Video #" + String.valueOf(count) + ")");
				if (videoEntry.getTitle() != null) {
					Log.d(LOG_TAG, "Title: " + videoEntry.getTitle().getPlainText() + "\n");
				}
				if (videoEntry.getSummary() != null) {
					Log.d(LOG_TAG, "Summary: " + videoEntry.getSummary().getPlainText() + "\n");
				}
				YouTubeMediaGroup mediaGroup = videoEntry.getMediaGroup();
				if (mediaGroup != null) {
					MediaPlayer mediaPlayer = mediaGroup.getPlayer();
					if (mediaPlayer != null) {
						Log.d(LOG_TAG, "Web Player URL: " + mediaPlayer.getUrl());
					}
					MediaKeywords keywords = mediaGroup.getKeywords();
					if (keywords != null) {
						Log.d(LOG_TAG, "Keywords: ");
						for (String keyword : keywords.getKeywords()) {
							Log.d(LOG_TAG, keyword + ",");
						}
					}
					Log.d(LOG_TAG, "\n");
					Log.d(LOG_TAG, "\tThumbnails:");
					for (MediaThumbnail mediaThumbnail : mediaGroup.getThumbnails()) {
						Log.d(LOG_TAG, "\t\tThumbnail URL: " + mediaThumbnail.getUrl());
						Log.d(LOG_TAG, "\t\tThumbnail Time Index: " + mediaThumbnail.getTime());
						Log.d(LOG_TAG, "\n");
					}
					Log.d(LOG_TAG, "\tMedia:");
					for (YouTubeMediaContent mediaContent : mediaGroup.getYouTubeContents()) {
						Log.d(LOG_TAG, "\t\tMedia Location: " + mediaContent.getUrl());
						Log.d(LOG_TAG, "\t\tMedia Type: " + mediaContent.getType());
						Log.d(LOG_TAG, "\t\tDuration: " + mediaContent.getDuration());
						Log.d(LOG_TAG, "\n");
					}
					Log.d(LOG_TAG, "\n");
				}
				count++;
			}

		} catch (Exception e) {
			Log.e(LOG_TAG, "getFeed", e);
		}
	}

	/**
	 * @return list of YouTube video Id's
	 */
	public ArrayList<String> getVideos() {
		return videos;
	}
}
