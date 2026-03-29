package fa.nfa;
import fa.State;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class NFAState<S> extends State {
    private final Map<S, NFAState<S>> transitionMap;

    public NFAState(String name) {
        super(name);
        transitionMap = new HashMap<>();
    }

    public void addTransition(S key, NFAState<S> destination) throws UnsupportedOperationException {
        if (transitionMap.containsKey(key)) throw new UnsupportedOperationException("Attempted to override existing delta");
        transitionMap.put(key, destination);
    }

    public void clearTransition(S key) throws NoSuchElementException {
        if (!transitionMap.containsKey(key)) throw new NoSuchElementException("Attempted to clear non-existent delta");
        transitionMap.remove(key);
    }

     /**
     * Returns the NFAState object that results from a transition
     * @param key                       The character consumed in a transition
     * @return                          The NFAState that results when traversing a particular transition
     * @throws NoSuchElementException   Thrown when there is an attempt to get the destination of a key/destination pair that does not exist
     */
    public NFAState<S> getTransition(S key) throws NoSuchElementException{
        if (!transitionMap.containsKey(key)) throw new NoSuchElementException("Attempted to get non-existent delta");

        return transitionMap.get(key);
    }

    public boolean hasTransition(S key) {
        return transitionMap.containsKey(key);
    }

    public void clearAllTransitions() {
        transitionMap.clear();
    }

    public boolean isEpsilonTransition(S key) {
        return key.equals("e");
    }

}
