package com.hititcs.dcs;

import com.hititcs.dcs.domain.model.FlightSummary;
import com.hititcs.dcs.view.baggagetracking.domain.model.ScannedTag;
import org.parceler.ParcelClass;
import org.parceler.ParcelClasses;

@ParcelClasses({
    @ParcelClass(FlightSummary.class),
    @ParcelClass(ScannedTag.class)
})
public class ParcelableClass {

  // empty. Domain model parcel generation
}
