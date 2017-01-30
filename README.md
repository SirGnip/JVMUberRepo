# roadmap

Iteration #1 - minimal features so I use it instead of Python one

- x make Receive bigger so it is easy to click
- double click to receive item
- two store modes:
    - by append and by replace (what does this look like with multiselect?)
    - separete buttons? right click button to change mode? checkbox somehwere? toggle in Settings menu item?)
- delete selected
- add menus
	- file
	    - exit
	- clip
		- deleted selected
- truncate text display for very long items or ones with embedded cr's?
		
# use cases

- create clips
    - read form clipboard, file, url, list of files
- manipulating list of clips
    - append, replace, remove, reorder, sort, clear, filter, export to disk
- mutating clip contents
    - prepend, append, join, split, filter, sort, count, variable substitute, numbering
    - able to apply mutations to individual clips or to all selected clips?
- retrieving clips
    - single
    - multiple selected
        - cycle through all/selected?
        - join w/ provided delimiter
- destination of receive
    - clipboard, file, post to url, substitute into shell command?
    
uncategorized
    
- java clipboard toolbox UI
    - able to split, join, prepend, append, replace, sort, filter, columns, graph, clipboard stack, etc.
    - variable substitution?
    - push and retrieve from other buffers
    - able to treat buffers and deque (push/pop from front and back) and random access (read from anywehre)
    - clipboard stack/cache/manipulator
    - manage a clipboard stack
    - allow modifications (regex, substitution, replacement, stripping, append/prepend, numbering) to clipboard in memory and 
clipboard buffers
    - different ways of getting text in (take a multi-line selection and put each line into a separate bucket? separate by a comma?)
        - drag and drop replaces
        - drag and drop appends (maybe with optional delimiter)
    - different ways of getting text out (clipboard, save to file, POST to URL?)
        - post clipboard to url
        - use variable substitution to open a url (ex: http://www.stuff.com/doit/%{1}
    - variable substitution
        - be able to independently set values for aaa=somestring bbb=other and substitute those into a sequence of clips 
(if clip contains "this is ${aaa} for ${bbb}". 
    - open url aftre substituting current clipboard into a url (ex: http://docs.oracle.com/javase/8/docs/api/javax/swing/SpinnerDateModel.htm
l (any way to search for docs given just a class/interface name?)



# modification
- what is input? output?

- clip operations (modifies selected clip)
    - prepend, append
        - in: string 
        - output: selected clip
- line operations (treats selected clip as a list of lines with each line being one item)
    - sort lines (tokenizes by lines)
        - in: selected clip
        - out: selected clip
    - filter lines (tokenize by lines)

# clipboard todo
- maybe list out core use cases (broad, but not exhaustive)
    - list bare-bones features to replace my python implmenetation so i will actually start using it
    - then list ist *must* have features (the core, fun features like append, split, join, sort filter)
    - have a second list for "maybe" (like ability to control truncation of )

- double click on item does a "retrieve" of that item into clipboard
    - what about multi-select?
- with multi-select, maybe retrieve becomes multi-select aware, where it merges the multiple lines together with a given token (empty, space, carriage return, tab, comma)
    - how do i reconcile this with doing a multi-select to be used in a cyclical retrieve?  THe assumption would be that a cyclical retrieve would be bounded by what is selected? Maybe two modes (merge and cycle) of retrieve? When in cycle mode, how does UI track what clip is current?
     

# Feature details

### Cycling through clipboard buffer
 
Visualizing focus ideas as when the ListView control as a whole looses focus, the individual cell focus (thin border) is not shown. So, what are other ways I can visualize the current cell while cycling?  Here are brainstorms:

- force the control focus to always be on the ListView, which shows the selected items and draws a border around the item with focus.
- create global css file for UI that still shows the focused cell item even if the ListView doesn't have the focus.  When the ListView doesn't have the focus, it uses gray instead of blue and no border around focused item (probably because three is no focus on the ListView!). This snippet is a starting point, but doesn't do what I want.
    - `items.setStyle("-fx-selection-bar: red; -fx-selection-bar-non-focused: blue; -fx-selection-bar-text: black");`

- do some manual animation on the cell to highlight it
- add some text to the data model if it has focus
- have another control (anothre ListView) next to it in parallel that highlights the current cycle row somehow?
- add a status bar at the bottom of the screen and have it show the buffer ID with the focus and preview of it
- just not worry about having a nice visualization for cycling for now
