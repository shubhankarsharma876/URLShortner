package shortUrl.URLShortner.bloomFilter;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.function.Function;

public class BloomFilter <T>{
    private final BitSet bitSet;
    private final int bitArraySize;

    private final int numberOfHashFunctions;
    private final Function<T,Integer>[] hashFunctions;

    @SafeVarargs
    public BloomFilter(int bitArraySize, int numberOfHashFunctions, Function<T, Integer>... hashFunctions) {
        this.bitSet = new BitSet(bitArraySize);
        this.bitArraySize = bitArraySize;
        this.numberOfHashFunctions = numberOfHashFunctions;
        this.hashFunctions = hashFunctions;
    }

    public void add (T item){

        //Now we should check for the hashvalue with the use of all the three hashing function
        for(Function<T,Integer> hashFunction:hashFunctions){
            int hash = Math.abs(hashFunction.apply(item)%bitArraySize);
            bitSet.set(hash,true); //Here we will be set the bit at the position for which we found the hashvalue.
        }
    }

    public boolean mightContain(T item){
        for(Function<T,Integer> hashFunction:hashFunctions){
            int hash = Math.abs(hashFunction.apply(item)%bitArraySize);
            if(!bitSet.get(hash)){
                return false;
            }
        }
        return true;
    }

    public static int hash1(String item) {
        return item.hashCode();
    }

    public static int hash2(String item) {
        return item.getBytes(StandardCharsets.UTF_8).length;
    }

    public static int hash3(String item) {
        return item.length();
    }



}
