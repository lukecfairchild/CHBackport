THIS EXTENSION IS NO LONGER BEING MAINTAINED, AND SHOULD NOT BE USED.

The server\_command event is added to the CH core, and is available without extension. The tmp_file_list_dir function is available in the CHFiles extension, which can be found [here](https://github.com/itstake/CHFiles) and [here](https://letsbuild.net/jenkins/job/CHFiles/).

# Functions
## array tmp\_file\_list\_dir(path):
Returns a list of files and folders in the specified directory. If the specified path isn't a directory, an IOException is thrown.
# Events
## server\_command
This event is fired off when any command is run from the console. This actually fires before normal  CommandHelper aliases, allowing you to insert control before defined aliases, even. Be careful with this event, because it overrides ALL console commands, which if you aren't careful can cause all sorts of havok, because the command is run as console, which is usually completely unrestricted.
### Prefilters
**command**: <String Match> The entire command the console ran  
**prefix**: <String Match> Just the first part of the command, i.e. '/cmd' in '/cmd blah blah'
### Event Data
**command**: The entire command  
**prefix**: Just the prefix of the command
### Mutable Fields
**command**
