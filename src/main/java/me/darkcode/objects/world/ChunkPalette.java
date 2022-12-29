package me.darkcode.objects.world;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Objects;

public class ChunkPalette {

    private BitSet bitSet;
    private AbstractBlockData[] values;

    public ChunkPalette(BitSet bitSet, AbstractBlockData[] values) {
        this.bitSet = bitSet;
        this.values = values;
    }

    public ChunkPalette(int bitsPerValue, AbstractBlockData defaultObject) {
        bitSet = new BitSet(bitsPerValue * 4096);
        values = new AbstractBlockData[]{defaultObject};
    }

    public BitSet getBitSet() {
        return bitSet;
    }

    public AbstractBlockData[] getValues() {
        return values;
    }

    public AbstractBlockData getValue(int x, int y, int z) {
        return values[getBitValue((x & 15) + (z & 15) * 16 + (y & 15) * 256, getBitsPerValue(), bitSet)];
    }

    public void setValue(int x, int y, int z, AbstractBlockData value){
        AbstractBlockData currentValue = getValue(x, y, z);
        if(currentValue.equals(value)){
            return;
        }
        int index = getValueIndex(value);
        int bitIndex = (x & 15) + (z & 15) * 16 + (y & 15) * 256;
        int bitsPerValue = getBitsPerValue();
        if(index == -1){
            int maxValues = (int) Math.pow(2, bitsPerValue);
            if(maxValues > values.length){
                AbstractBlockData[] newValues = Arrays.copyOf(values, values.length+1);
                newValues[newValues.length-1] = value;
                values = newValues;
                setBitValue(bitIndex, bitsPerValue, newValues.length-1, bitSet);
            }else if(maxValues == values.length){
                remap(++bitsPerValue);
                AbstractBlockData[] newValues = Arrays.copyOf(values, values.length+1);
                newValues[newValues.length-1] = value;
                values = newValues;
                setBitValue(bitIndex, bitsPerValue, newValues.length-1, bitSet);
            }
        }else{
            setBitValue(bitIndex, bitsPerValue, index, bitSet);
        }
        if(usage(getValueIndex(currentValue))==0){
            values[getValueIndex(currentValue)] = null;
            remapValues();
            int minValues = (int) Math.pow(2, bitsPerValue-1);
            if(minValues >= values.length){
                remap(--bitsPerValue);
            }
        }
    }

    private void remapValues() {
        AbstractBlockData[] tempCopy = values.clone();
        HashMap<Integer, Integer> mapping = new HashMap<>();
        int freeSlot = values.length-1;
        while(values[freeSlot] == null){
            freeSlot--;
        }
        for(int i = 0; i < values.length; i++){
            if(values[i] == null){
                tempCopy[i] = values[freeSlot];
                tempCopy[freeSlot] = null;
                mapping.put(freeSlot, i);
                while(values[freeSlot] == null && freeSlot > i+1){
                    freeSlot--;
                }
            }
        }
        int bitsPerValue = getBitsPerValue();
        for(int i = 0; i < 4096; i++){
            int bit = getBitValue(i, bitsPerValue, bitSet);
            Integer newBit = mapping.get(bit);
            if(newBit != null){
                setBitValue(i, bitsPerValue, newBit, bitSet);
            }
        }
        values = Arrays.stream(tempCopy).filter(Objects::nonNull).toArray(AbstractBlockData[]::new);
    }

    private int usage(int bitValue){
        int bitsPerValue = getBitsPerValue();
        int usage = 0;
        for(int i = 0; i < 4096; i++){
            if(getBitValue(i, bitsPerValue, bitSet) == bitValue){
                usage++;
            }
        }
        return usage;
    }

    private int getValueIndex(AbstractBlockData value){
        for(int i = 0; i < values.length; i++){
            AbstractBlockData object = values[i];
            if(object.equals(value)){
                return i;
            }
        }
        return -1;
    }

    private int getBitValue(int bitIndex, int bitsPerValue, BitSet set) {
        int value = 0;
        int bitStart = bitIndex * bitsPerValue;
        for (int i = 0; i < bitsPerValue; i++) {
            value += (set.get(bitStart + i) ? Math.pow(2, bitsPerValue - i - 1) : 0);
        }
        return value;
    }

    private void setBitValue(int bitIndex, int bitsPerValue, int value, BitSet set){
        int bitStart = bitIndex * bitsPerValue;
        String data = Integer.toBinaryString(value);
        data = data.length() < bitsPerValue ? ("0".repeat(bitsPerValue-data.length()) + data) : data;
        for(int i = 0; i < bitsPerValue; i++){
            set.set(bitStart+i, data.charAt(i) == '1');
        }
    }

    private void remap(int toBitsPerValue){
        BitSet newBitSet = new BitSet(toBitsPerValue*4096);
        for(int i = 0; i < 4096; i++){
            setBitValue(i, toBitsPerValue, getBitValue(i, getBitsPerValue(), bitSet), newBitSet);
        }
        this.bitSet = newBitSet;
    }

    public int getBitsPerValue() {
        return bitSet.size() / 4096;
    }

}