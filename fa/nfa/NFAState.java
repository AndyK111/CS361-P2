package fa.nfa;
import fa.State;
import java.util.*;

/**
 * Represents a distinct state in an NFA. Each NFAState has a name and a set of transitions to other NFAStates, 
 * stored as a HashMap where the keys are the symbols consumed in the transition and the values are sets of NFAStates
 * reachable by comsuming the corresponding symbol (or epsilon if an epsilon transition). This class includes
 * methods for adding, clearing, and retrieving transitions, as well as checking if a transition exists or is an 
 * epsilon transition.
 * @author Sam Kleman, Andy Kempf
 */
@SuppressWarnings("unused")
public class NFAState extends State {
    // Transition map responsible for storing the state's transitions
    private final Map<Character, Set<NFAState>> transitionMap;

    /**
     * Constructor for NFAState, initializes instance variables and sets the name of the state
     * @param name - the name of the state
     */
    public NFAState(String name) {
        super(name);
        transitionMap = new HashMap<>();
    }

    /**
     * Adds a transition to the NFAState's transition map, creating a new entry if the key does not already exist
     * @param key - the symbol consumed in the transition
     * @param destination - the NFAState reachable by consuming the symbol
     */
    public void addTransition(Character key, NFAState destination) {
        transitionMap
            .computeIfAbsent(key, k -> new HashSet<>())
            .add(destination);
    }

    /**
     * Clears the transition associated with the given key from the NFAState's transition map
     * @param key - the symbol for which to clear the transition
     * @throws NoSuchElementException - if the transition does not exist
     */
    public void clearTransition(Character key) throws NoSuchElementException {
        if (!transitionMap.containsKey(key)) throw new NoSuchElementException("Attempted to clear non-existent delta");
        transitionMap.remove(key);
    }

     /**
     * Returns the NFAState object that results from a transition
     * @param key - The character consumed in a transition
     * @return - The NFAState that results when traversing a particular transition
     * @throws NoSuchElementException - If the transition does not exist
     */
    public Set<NFAState> toStates(Character key) throws NoSuchElementException {
        return transitionMap.get(key) != null ? new HashSet<>(transitionMap.get(key)) : new HashSet<>();
    }

    /**
     * Checks if a transition exists for the given key in the transition map
     * @param key - The symbol for which to check the transition
     * @return - True if the transition exists, false otherwise
     */
    public boolean hasTransition(Character key) {
        return transitionMap.containsKey(key);
    }

    /**
     * Clears all transitions from the NFAState's transition map
     */
    public void clearAllTransitions() {
        transitionMap.clear();
    }

    /**
     * Checks if the given key corresponds to an epsilon transition (represented by "e")
     * @param key - The symbol to check
     * @return - True if the key represents an epsilon transition, false otherwise
     */
    public boolean isEpsilonTransition(Character key) {
        return key.equals('e');
    }

    /**
     * Returns a set of all NFAStates reachable from this state through any transition (including epsilon transitions)
     * @return - A set of all reachable NFAStates
     */
    public Set<NFAState> getAllTransitions() {
        Set<NFAState> result = new HashSet<>();
        for (Set<NFAState> states : transitionMap.values()) {
            result.addAll(states);
        }
        return result;
    }

}
