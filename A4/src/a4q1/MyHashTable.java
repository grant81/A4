package a4q1; //Yingnan Zhao 260563769

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

class MyHashTable<K, V> implements Iterable<MyHashTable<K, V>.HashEntry> {
	/*
	 * Number of entries in the HashTable.
	 */
	private int entryCount = 0;

	/*
	 * Number of buckets. The constructor sets this variable to its initial
	 * value, which eventually can get changed by invoking the rehash() method.
	 */
	private int numBuckets;

	/**
	 * Threshold load factor for rehashing.
	 */
	private final double MAX_LOAD_FACTOR = 0.75;

	/**
	 * buckets to store the key-value pairs. Traditionally an array is used for
	 * the buckets and a linked allEntries is use for the entries within each
	 * bucket.
	 * 
	 * We use an ArrayallEntries rather than array, since the former is simpler
	 * to use in Java.
	 */

	ArrayList<LinkedList<HashEntry>> buckets;

	/*
	 * Constructor.
	 * 
	 * numBuckets is the initial number of buckets used by this hash table
	 */
	/*
	 * first i set the number of bucket equals to the value that being passed
	 * in, then create the buckets array list that has a size of the numBuckets
	 * by adding empty linkedlist to a temp array list, and then set the bucket
	 * array list equals to the temp list just to make the space
	 */
	MyHashTable(int numBuckets) {

		// ADD YOUR CODE HERE
		this.numBuckets = numBuckets;
		this.buckets = new ArrayList<LinkedList<HashEntry>>(numBuckets);

		//
		ArrayList<LinkedList<HashEntry>> temp = new ArrayList<>();

		for (int i = 0; i < numBuckets; i++) {

			LinkedList<HashEntry> linktemp = new LinkedList<>();
			temp.add(linktemp);
		}
		this.buckets = temp;
	}

	/**
	 * Given a key, return the bucket position for the key.
	 */
	private int hashFunction(K key) {

		return Math.abs(key.hashCode()) % numBuckets;
	}

	/**
	 * Checking if the hash table is empty.
	 */
	public boolean isEmpty() {
		if (entryCount == 0)
			return true;
		else
			return (false);
	}

	/**
	 * return the number of entries in the hash table.
	 */
	public int size() {
		return (entryCount);
	}

	/**
	 * Adds a key-value pair to the hash table. If the load factor goes above
	 * the MAX_LOAD_FACTOR, then call the rehash() method after inserting.
	 * 
	 * If there was a previous value for the given key in this hashtable, then
	 * return it. Otherwise return null.
	 */
	/*
	 * if the key and the value is already stored in the hashtable return the
	 * value create a temp hash entry ha, and store the key and hte value into
	 * it get the index by calling the hashFunction method and store to the very
	 * position by addlast the the linked list stored at the position increase
	 * the entryCount if the load_Factor is larger than the max call rehash
	 * function
	 */
	public V put(K key, V value) {

		// ADD YOUR CODE HERE
		HashEntry ha = new HashEntry(key, value);
		if (containsKey(key)) {
			return value;
		} else {
			int index = hashFunction(key);
			buckets.get(index).addLast(ha);
			entryCount++;
			if ((float) entryCount / numBuckets >= MAX_LOAD_FACTOR) {
				rehash();
			}
			return null;

		} // remove this stub
	}

	/**
	 * Retrieves a value associated with some given key in the hash table.
	 * Returns null if the key could not be found in the hash table)
	 */
	/*
	 * if the key entry we got from calling the getEntry function is null return
	 * null else get the value by calling the getEntry function
	 * 
	 */
	public V get(K key) {

		// ADD YOUR CODE HERE
		if (getEntry(key) != null) {
			return getEntry(key).getValue();
		} else {
			return null;
		}
	}

	/**
	 * Removes a key-value pair from the hash table. Return value associated
	 * with the provided key. If the key is not found, return null.
	 */
	/*
	 * iterate through all the nodes in the in the linkedList we retrieved
	 * according to the hashFunction from the bucketList if the key value
	 * matches, remove the node and return it. if not return null
	 */
	public V remove(K key) {

		// ADD YOUR CODE HERE
		int index = hashFunction(key);
		LinkedList<HashEntry> bucketList = buckets.get(index);
		for (HashEntry node : bucketList) {
			if (node.getKey().equals(key)) {
				bucketList.remove(node);
				return node.getValue();
			}

		}
		return (null);
	}

	/*
	 * This method is used for testing rehash(). Normally one would not provide
	 * such a method.
	 */
	public int getNumBuckets() {
		return numBuckets;
	}

	/*
	 * Returns an iterator for the hash table.
	 */

	// @Override
	public MyHashTable<K, V>.HashIterator iterator() {
		return new HashIterator();
	}

	/**
	 * Removes all the entries from the hash table, but keeps the number of
	 * buckets intact.
	 */
	public void clear() {
		for (int ct = 0; ct < buckets.size(); ct++) {
			buckets.get(ct).clear();
		}
		entryCount = 0;
	}

	/**
	 * Create a new hash table that has twice the number of buckets.
	 */
	/*
	 * first create a MyHashTable object with two times the size of the old one
	 * called new_table, then iterate through the key and value pair and store
	 * them into the new_table, and then set the bucket list equals to the
	 * bucket of the new table, increase the numBuckets by two times
	 */
	public void rehash() {
		// ADD YOUR CODE HERE
		MyHashTable<K, V> new_table = new MyHashTable<K, V>(numBuckets * 2);
		ArrayList<K> listKeys = keys();
		ArrayList<V> listValues = values();
		for (int i = 0; i < entryCount; i++) {
			K ktemp = listKeys.get(i);
			V vtemp = listValues.get(i);
			new_table.put(ktemp, vtemp);
		}
		this.buckets = new_table.buckets;
		this.numBuckets = numBuckets * 2;
	}

	/*
	 * Checks if the hash table contains the given key. Return true if the hash
	 * table has the specified key, and false otherwise.
	 */
	public boolean containsKey(K key) {
		return (get(key) != null);
	}

	/*
	 * return an ArrayList of the keys in the hashtable
	 */
	/*
	 * use a double linkedlist to iterate through the bucket list and store all
	 * the keys into the listKeys array list
	 */
	public ArrayList<K> keys() {

		ArrayList<K> listKeys = new ArrayList<K>();

		// ADD YOUR CODE HERE
		for (int i = 0; i < buckets.size(); i++) {
			LinkedList<HashEntry> bucketList = buckets.get(i);
			for (HashEntry node : bucketList) {
				listKeys.add(node.getKey());
			}
		}

		return listKeys;

	}

	/*
	 * return an ArrayList of the values in the hashtable
	 */
	/*
	 * use a double linkedlist to iterate through the bucket list and store all
	 * the values into the listValues array list
	 */
	public ArrayList<V> values() {
		ArrayList<V> listValues = new ArrayList<V>();

		// ADD YOUR CODE HERE
		for (int i = 0; i < buckets.size(); i++) {
			LinkedList<HashEntry> bucketList = buckets.get(i);
			for (HashEntry node : bucketList) {
				listValues.add(node.getValue());
			}
		}
		return listValues;
	}

	@Override
	public String toString() {
		/*
		 * Implemented method. You do not need to modify.
		 */
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buckets.size(); i++) {
			sb.append("Bucket ");
			sb.append(i);
			sb.append(" has ");
			sb.append(buckets.get(i).size());
			sb.append(" entries.\n");
		}
		sb.append("There are ");
		sb.append(entryCount);
		sb.append(" entries in the hash table altogether.");
		return sb.toString();
	}

	/*
	 * Inner class: Iterator for the Hash Table.
	 */
	public class HashIterator implements Iterator<HashEntry> {
		LinkedList<HashEntry> allEntries;

		/**
		 * Constructor: make a linkedlist 'allEntries' of all the entries in the
		 * hash table
		 */
		/*
		 * use a double for loop to iterate through the bucket list and store
		 * every node into the allEntries linked list
		 */
		public HashIterator() {
			allEntries = new LinkedList<HashEntry>();
			for (int i = 0; i < buckets.size(); i++) {
				LinkedList<HashEntry> bucketList = buckets.get(i);
				for (HashEntry node : bucketList) {
					if (node != null) {
						allEntries.addLast(node);
					}
				}
			}

		}

		// Override
		@Override
		public boolean hasNext() {
			return !allEntries.isEmpty();
		}

		// Override
		@Override
		public HashEntry next() {
			return allEntries.removeFirst();
		}

		@Override
		public void remove() {

			// not implemented, but must be declared because it is in the
			// Iterator interface

		}
	}

	// helper method

	private HashEntry getEntry(K key) {
		int index = hashFunction(key);
		LinkedList<HashEntry> bucketList = buckets.get(index);
		for (HashEntry node : bucketList) {
			if (node.getKey() == key)
				return node;
		}
		return null;
	}

	class HashEntry {

		private K key;
		private V value;

		/*
		 * Constructor.
		 */
		HashEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		/*
		 * Returns this hash entry's key. Assume entry is not null.
		 * 
		 * @return This hash entry's key
		 */
		K getKey() {
			return (key);
		}

		/**
		 * Returns this hash entry's value. Assume entry is not null.
		 */
		V getValue() {
			return (value);
		}

		/**
		 * Sets this hash entry's value.
		 */
		void setValue(V value) {
			this.value = value;
		}
	}

}
