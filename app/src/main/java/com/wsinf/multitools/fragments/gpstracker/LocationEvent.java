package com.wsinf.multitools.fragments.gpstracker;

public interface LocationEvent
{
    void onLocationChange(final double latitude, final double longitude);
}
