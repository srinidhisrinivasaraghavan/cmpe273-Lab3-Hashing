package edu.sjsu.cmpe.cache.client;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.common.hash.Hashing;

public class ConsistentHashing<CacheServiceInterface> {
	private SortedMap<Integer, CacheServiceInterface> circle = new TreeMap<Integer, CacheServiceInterface>();
	public void add(CacheServiceInterface server) {
		int index = server.toString().lastIndexOf(':');
		int key = Integer.parseInt(server.toString().substring(index + 1));
		circle.put(Hashing.md5().hashLong(key).asInt(), server);
	}
	public CacheServiceInterface get(int key) {
		if (circle.isEmpty()) {
			return null;
		}
		int hash = Hashing.md5().hashLong(key).asInt();
		if (!circle.containsKey(hash)) {
			SortedMap<Integer, CacheServiceInterface> tailMap = circle.tailMap(hash);
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		return circle.get(hash);
	}
	public int getSize() {
		return circle.size();
	}

}

