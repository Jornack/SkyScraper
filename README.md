This is my fork of [Jornack/SkyScraper](https://github.com/Jornack/SkyScraper).

Overview
--------

SkyScraper was inspired by ericblue's Mindstream and brain_grapher (https://github.com/ericblue).
Brain_grapher provided me the graphs, but couldn't log. Mindstream could log, but didn't show any graphs. So, basically I just combined the functionality of both applications. 

I originally developed and tested this application with the MindWave and Thinkgear 2.2.0.0, java version "1.6.0_26" in Win7x64 Ultimate. I assume it works for the MindSet as well, but I don't own a MindSet so I'm not sure. Let me know if it does or does not :)

The code is a mess, I know. I started off working on ericblue's code, but then I decided to go GUI, adding and removing stuff....and I basically ended up with a whole different application. It has been about seven years since I did any java coding. So, I was basically relearning this as I progressed :D

Changes
-------
This fork includes the following patches

- Export rawEeg signal to the csv file.
- Fixed double commited src folder
- removed donate button (I don't want to adorn myself with borrowed plumes)

Features
--------

- Shows realtime data as Bar Chart or Line Chart.
- Saves data to a file on the local filesystem
- Generates a JPG/PNG file of a saved session.
- Converts a saved session into Excel or CSV format

Screenshots
-----------

https://github.com/Jornack/SkyScraper/raw/master/images/screenshot-01.png
https://github.com/Jornack/SkyScraper/raw/master/images/screenshot-02.png
https://github.com/Jornack/SkyScraper/raw/master/images/generated%20-%20skyskraper.20120210%2019.19.3412.json%20.png

Dependencies
------------
Copy the following jar-files in the lib folder of this project and add them to you buildpath.

* jcommon
* jfreechart
* json
* junit
* swtgraphics2s
* jexcelapi

Issues
------

The Disconnect button doesn't switch back to Connect when the connection is terminated by Thinkgear (i.e. ThinkGear crashes)

ToDo
----

- Add statistics tab
- Add replay of saved session
- Add preload of saved session before generation so user can select specific parts of the session to generate.
- Add option to skip the savedialog and start saving immediately
- Make fancy Signal indicator [Done]
- Add fancy icon [Done]
- Add Excel/CSV conversion [Done]
- Add option to set  timeline in LineChart
- Redo the GUI
- Clean up code
- Add menu
- Refactor all code :$

Change log
----------
- Added rawEeg signal to CSV export
- Added Excel/CSV conversion
- Added fancy Signal indicator
19 feb 2012 - Initial release v1.0b
18 feb 2012 - Initial release v1.0a
