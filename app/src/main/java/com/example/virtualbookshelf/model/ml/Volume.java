package com.example.virtualbookshelf.model.ml;

/**
 * The Volume class represents a volume in the Google Books API.
 */
public class Volume {
    /**
     * VolumeInfo object
     */
    private VolumeInfo volumeInfo;

    /**
     * Volume info getter
     * @return Volume info
     */
    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    /**
     * Volume info setter
     * @param volumeInfo Volume info
     */
    public void setVolumeInfo(VolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }
}
