package com.example.android.tourguide;

public class Tour {
    //define string name of place
    private String mNameOfPlace;
    //define string address
    private String mAddress;
    //define resource id for image
    private int mImageResourceId = NO_IMAGE_PROVIDED;
    //define audio resource id
    private int mAudioResourceId;
    /**
     * Constant value that represents no image was provided for this word
     */
    private static final int NO_IMAGE_PROVIDED = -1;


    /**
     * create new Tour constructor
     *  @param nameOfPlace     is name of place
     * @param address         is address
     * @param audioResourceId is resource id of audio
     * @param imageResourceId is resource id of image
     */
    public Tour(String nameOfPlace, String address, int audioResourceId, int imageResourceId) {
        mNameOfPlace = nameOfPlace;
        mAddress = address;
        mAudioResourceId = audioResourceId;
        mImageResourceId = imageResourceId;
    }

    /**
     * @return name of place
     */
    public String getmNameOfPlace() {
        return mNameOfPlace;
    }

    /**
     * @return address of place
     */
    public String getmAddress() {
        return mAddress;
    }

    /**
     * @return resource id of image
     */
    public int getmImageResourceId() {
        return mImageResourceId;
    }

    /**
     * check if an image is exist in list item
     */
    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }

    /**
     * @return resource id of audio
     */
    public int getmAudioResourceId() {
        return mAudioResourceId;
    }


    @Override
    public String toString() {
        return "Tour " +
                "mNameOfPlace = " + mNameOfPlace + '\''
                + ",mAddress = " + mAddress + '\'' +
                ",mImageResourceId= " + mImageResourceId +
                '\'' + ",mAudioResourceId= " + mAudioResourceId +
                '\'' + '}';
    }
}
