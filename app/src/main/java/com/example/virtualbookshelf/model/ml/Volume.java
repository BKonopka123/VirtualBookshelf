package com.example.virtualbookshelf.model.ml;

/**
 * The Volume class represents a volume in the Google Books API.
 */
public class Volume {
    /**
     * Volume id
     */
    private String id;
    /**
     * VolumeInfo object
     */
    private VolumeInfo volumeInfo;

    /**
     * Volume id getter
     * @return Volume id
     */
    public String getId() { return id; }

    /**
     * Volume id setter
     * @param id Volume id
     */
    public void setId(String id) { this.id = id; }

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
