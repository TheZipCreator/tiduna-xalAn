# Source
Contains source code for the jars in ..

**NOTE:** There's not much error checking in many of these (since they're meant to be quick-and-dirty scripts) so if you give them erroneous inputs they may crash or give erroneous results.

The source code is licensed with the GNU General Public License Version 3.

You can build these with kotlinc like so:
`kotlinc phoneticize.kt parser.kt -include-runtime -d ../phoneticize.jar`
`kotlinc write.kt parser.kt -include-runtime -d ../write.jar`
