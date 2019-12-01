package com.wsinf.spytracker.location;

public interface LocationEvent
{
    void onLocationChange(final double latitude, final double longitude);
}
