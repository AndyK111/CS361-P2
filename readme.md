****************
* Project number/name: Project 2
* Class: CS361 
* Date: 03/30/2003
* Your name: Andy Kempf, 
**************** 


# OVERVIEW
The program includes a library used to simulate an NFA. This library builds upon the capability
of a DFA by allowing epsilon transitions to exist, and states now map to sets of stats on a transition, allowing
transitions from one input to multiple states.

# INCLUDED FILES

These are the files included (in addition to project files like interface/abstract classes, etc...)
- `NFA.java` : Class responsible for managing groups of states and traversions.
- `NFAState.java` : Class responsible for managing data designated to a state, primarily transitions.
- `NFATest.java` : JUnit tests for NFA.java integrity

# COMPILING AND RUNNING

This project does not have a `main` method or command-line interface. The
implementation is meant to be compiled as a library and exercised through the
JUnit test suite in `test/nfa/NFATest.java`.

# PROGRAM DESIGN AND IMPORTANT CONCEPTS

The program is divided up into a few sections, the constructor and fields
are self-explanatory. The trivial methods all relate to trivial data manipulation
and retrieval. The EPSILON constant refers to the letter `e` as per the assignment 
guidelines.

The core of the functionality in NFA.java rest in the eClosure, getToState, and getTraceMap method. 
The rest of the methods in the non-trivial region are typically taking data from one of these three
and performing some relatively simple operation upon it to yield the result.

eClosure is the lowest level of functionality here, the other two are built ontop of the work that 
eClosure does. eClosure uses an internal stack to keep track of what needs to be done next. It also has a HashSet
instance to keep track of the result of the work. It begins by pushing the provided state onto the stack,
then goes into a loop that processes until the stack is empty. Inside that loop we simply iterate through
all connections looking for any defined on e. And if that destination isnt in the set, we push it to the set and the 
workStack.

The next rung of the ladder is getToState. getToState takes a NFAState and a character, and systematically traverses the 
eclosure, and adds the reacable states through each node found in the eclosure. The eclosure of those
reachable states are added to the set of reachable states which are returned.

The next most complex function is the getTraceMap function. This one is not defined in the interface but proved to be
what I thought was the most elegant way to get the info needed for the other methods. This method works by generating a
HashMap that points an integer representing the number of character consumed to the different "NFA" instances after that
consumption (represented by states & the index number we are mapping from). It works by traversing of the input string
by indexxing, and simply assign the getToState result of consuming input from all previous states at the previous index
in the HashTable. The resulting table has a few interesting properties, the max cardinality of all the sets in its values
is equal to the max number of clones. The set pointed to by the length of the string contains all states reachable after
consuming all input, so checking for acceptance only entails iterating over this set and checking if any state is final.

isDFA is not involved in this chain of functionality. But its functionality is incredibly simple, NFAs have two 
properties that break the rules of the DFA, they have epsilon transitions, and they allow a character to transition to 
more than a single state. So isDFA just iterated over the set of all states, and returns false whenever it encounters
either a state with an epsilon transition, or a state with a transition that results in a set with greater than a single
element. 

# TESTING

Testing was done using the included test methods. There were some bugs, one such bug was
that I simply wasn't quite sure how to calculate the maxCopies. I tried experimenting with using
a record that would run on a findFinalStates helper that would iterate over everything and return the last
states. But tracking the maxCopies while using a more typical stack implementation proved to be very annoying, which is
why I rewrote to use the HashMap implementation it currently has. This involved lots of stepping in the debugger
before I gave up and rewrote, after rewriting it things fell into place nearly instantly.

# DISCUSSION

More or less an extension of the testing. I realized that I could just use a HashMap that correlated the remaining
input to what was reachable after consuming the character. At first it started as a String->Set<NFAState> map, but I
shortly realized that I could just replace the string with the amount of characters consumed, and then I also avoid 
having to take a subString everytime I generate a new layer. I chose this approach because it made
implementing the other methods trivial, all the data needed for acceptance and maxStates was there.

Working with stacks instead of recursion also proved difficult, but after coming to the realization that when you recurse
you're basically doing the same thing as pushing the parameters to the callstack and looping, it become fairly intuitive 
to reason about.

# EXTRA CREDIT

none


# SOURCES

I feel that the autocomplete on IntelliJ has gotten so good I should list it here. I dont know 
if it uses some sort of local AI or something, but it seemed to always know relatively what I was
about to type before I could finish thinking about it.

----------------------------------------------------------------------------


All content in a README file is expected to be written in clear English with
proper grammar, spelling, and punctuation. If you are not a strong writer,
be sure to get someone else to help you with proofreading. Consider all project
documentation to be professional writing for your boss and/or potential
customers.
