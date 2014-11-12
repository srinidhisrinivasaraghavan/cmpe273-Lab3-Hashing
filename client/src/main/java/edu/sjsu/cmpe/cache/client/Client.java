package edu.sjsu.cmpe.cache.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;

public class Client {
	public static void main(String[] args) throws Exception {
		System.out.println("Starting Cache Client...");
		CacheServiceInterface cache1 = new DistributedCacheService("http://localhost:3000");
		CacheServiceInterface cache2 = new DistributedCacheService("http://localhost:3001");
		CacheServiceInterface cache3 = new DistributedCacheService("http://localhost:3002");
		CacheServiceInterface cache = null;
		ConsistentHashing<CacheServiceInterface> nodeLookUp = new ConsistentHashing<CacheServiceInterface>();
		nodeLookUp.add(cache1);
		nodeLookUp.add(cache2);
		nodeLookUp.add(cache3);
		Map<Integer, String> data = new HashMap<Integer, String>();
		data.put(1, "a");
		data.put(2, "b");
		data.put(3, "c");
		data.put(4, "d");
		data.put(5, "e");
		data.put(6, "f");
		data.put(7, "g");
		data.put(8, "h");
		data.put(9, "i");
		data.put(10, "j");
		String dataValue = "";
		int bucket = 0;
		for (int i = 1; i <= 10; i++)
		{
			dataValue = data.get(i);
			bucket = Hashing.consistentHash(Hashing.md5().hashString(Integer.toString(i)),nodeLookUp.getSize());
			cache = nodeLookUp.get(bucket);
			System.out.println("Put " + dataValue+ " into server "+ cache.toString());
			cache.put(i, dataValue);
		}

		for (int i = 1; i <= 10; i++)
		{
			dataValue = data.get(i);
			bucket = Hashing.consistentHash(Hashing.md5().hashString(Integer.toString(i)),nodeLookUp.getSize());
			cache = nodeLookUp.get(bucket);
			System.out.println("Get from server "+ cache.toString() + " : " + cache.get(i));
		}

		System.out.println("Exiting Cache Client...");
	}

}


