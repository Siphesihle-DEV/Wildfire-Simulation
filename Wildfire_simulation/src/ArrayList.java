/**
 * ArrayList - A generic dynamic array implementation.
 * 
 * This class provides a resizable array data structure that automatically grows
 * when elements are added. It is used throughout the wildfire simulation
 * project for storing edges, paths, and nodes.
 * 
 * This is a custom implementation (not using java.util.ArrayList) as required
 * by the mini-project rules.
 * 
 * @param <T> the type of elements stored in this list
 * @author Clean_Code
 * @version 1.0
 */
public class ArrayList<T> {

	private int capacity;
	private T[] array;
	private int size;

	/**
	 * Constructs an empty ArrayList with an initial capacity of 1.
	 */
	public ArrayList() {
		this.capacity = 1;
		this.size = 0;
		this.array = createArray(capacity);
	}

	/**
	 * Creates a generic array of the specified size.
	 * 
	 * @param size the desired array size
	 * @return a new generic array of type T
	 */
	@SuppressWarnings("unchecked")
	private T[] createArray(int size) {
		return (T[]) new Object[size];
	}

	/**
	 * Returns the number of elements currently in the list.
	 * 
	 * @return the current size of the list
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns the element at the specified index.
	 * 
	 * @param index the index of the element to return
	 * @return the element at the specified position
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	public T get(int index) {
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException();
		return array[index];
	}

	/**
	 * Replaces the element at the specified index with the given value.
	 * 
	 * @param index the index of the element to replace
	 * @param value the new value to store at the specified index
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	public void set(int index, T value) {
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException();
		array[index] = value;
	}

	/**
	 * Inserts the specified element at the specified position in the list. Shifts
	 * all elements after the insertion point to the right.
	 * 
	 * @param index the index at which the element is to be inserted
	 * @param value the element to be inserted
	 */
	public void add(int index, T value) {

		if (size == capacity) {
			expand(); // Double the capacity when full
		}

		// Shift elements to the right to make space
		for (int i = size - 1; i >= index; i--) {
			array[i + 1] = array[i];
		}

		array[index] = value;
		size++;
	}

	/**
	 * Removes the element at the specified index and returns it. Shifts all
	 * subsequent elements to the left.
	 * 
	 * @param index the index of the element to be removed
	 * @return the removed element
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	public T remove(int index) {

		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}

		T removed = array[index];

		// Shift everything left to fill the gap
		for (int i = index; i < size - 1; i++) {
			array[i] = array[i + 1];
		}

		size--;

		return removed;
	}

	/**
	 * Doubles the capacity of the internal array when it becomes full. 	 
	 */
	private void expand() {
		capacity *= 2;
		T[] newArray = createArray(capacity);

		// Copy existing elements to the new larger array
		for (int i = 0; i < size; i++) {
			newArray[i] = array[i];
		}

		array = newArray;
	}
}