package nl.ru.ai.vroon.mdp;

/**
 * Enumerates the kinds of fields we can come across
 * @author Jered Vroon
 *
 */
public enum Field {
	OBSTACLE,		// i.e. this field cannot be visited
	EMPTY,			// i.e. this field can be visited
	REWARD,			// i.e. this field is an end state and yields positive reward
	NEGREWARD,		// i.e. this field is an end state and yields negative reward
	OUTOFBOUNDS		// i.e. this field does not exist
}
