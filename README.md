ChromeCastPi
============

<p><img src="http://chromecast.entertailion.com/chromecastanimation100.gif"/></p>

<p>ChromeCastPi is a command-line application to queue YouTube videos for <a href="https://www.google.com/intl/en/chrome/devices/chromecast/">ChromeCast</a> devices.
ChromeCastPi has been tested on a Raspberry Pi with a Java Runtime Environment and is ideal for scenarios where you want to automate the queueing of videos.</p>

<p>The application will get today's most popular videos using the <a href="https://developers.google.com/youtube/2.0/developers_guide_protocol_understanding_video_feeds">YouTube API</a>. The first video will be automatically loaded on the ChromeCast device and then paused.
You can use an Android app like <a href="https://play.google.com/store/apps/details?id=com.benlc.camcast">RemoteCast</a> to remotely control the media playback. When the video reaches the end, the next video in the queue will be automatically loaded.
The next video will also start if the video is stopped. The RemoteCast app can get out of sync with the videos; just use the ChromeCast icon at the top of the app to disconnect and re-connect to the device to get back in sync.
</p>

<p>To be able to run this application, follow these instructions:
<ul>
<li>Import this GIT project into Eclipse.</li> 
<li>Put your own YouTube API developer key in the Downloader.java file.</li>
<li>Use Eclipse to export the project as an executable JAR file.</li>  
<li>Run the application with the ChromeCast device IP address as the command line argument:
<blockquote>
java -jar chromecastpi.jar 192.168.0.22
</blockquote></li>  
</ul>
</p>

<p>There is a dependency on the <a href="https://github.com/entertailion/Caster">Caster</a> project. The JAR file of that project is included in the libs directory of this project. 
To create a new JAR file from the Caster project, export that project as a JAR file but don't include any of the files in it's libs directory since they are already part of this project.</p>

<p>Watch this <a href="http://youtu.be/RpGT8pakATs">video</a> to see the application in action.</p>

<p>Other apps developed by Entertailion:
<ul>
<li><a href="https://github.com/entertailion/Caster">Caster</a>: Command-line application and REST API to beam video files to ChromeCast devices.</li> 
<li><a href="https://github.com/entertailion/Fling">Fling</a>: GUI to beam video files from computers to ChromeCast devices.</li> 
<li><a href="https://github.com/entertailion/DIAL">DIAL</a>: ChromeCast device discovery and YouTube playback.</li>
<li><a href="https://play.google.com/store/apps/details?id=com.entertailion.android.tvremote">Able Remote for Google TV</a>: The ultimate Google TV remote</li>
<li><a href="https://play.google.com/store/apps/details?id=com.entertailion.android.launcher">Open Launcher for Google TV</a>: The ultimate Google TV launcher</li>
<li><a href="https://play.google.com/store/apps/details?id=com.entertailion.android.overlay">Overlay for Google TV</a>: Live TV effects for Google TV</li>
<li><a href="https://play.google.com/store/apps/details?id=com.entertailion.android.overlaynews">Overlay News for Google TV</a>: News headlines over live TV</li>
<li><a href="https://play.google.com/store/apps/details?id=com.entertailion.android.videowall">Video Wall</a>: Wall-to-Wall Youtube videos</li>
<li><a href="https://play.google.com/store/apps/details?id=com.entertailion.android.tasker">GTV Tasker Plugin</a>: Control your Google TV with Tasker actions</li>
</ul>
</p>