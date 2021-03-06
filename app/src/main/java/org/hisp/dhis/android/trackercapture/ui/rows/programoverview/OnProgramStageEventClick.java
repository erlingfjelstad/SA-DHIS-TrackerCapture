package org.hisp.dhis.android.trackercapture.ui.rows.programoverview;

import android.widget.ImageButton;

import org.hisp.dhis.android.sdk.persistence.models.Event;

/**
 * Created by erling on 5/28/15.
 */
public class OnProgramStageEventClick
{
    private final Event event;
    private final ImageButton hasFailedButton;
    private final boolean hasPressedFailedButton;
    private final int status;
    private final String errorMessage;

    public OnProgramStageEventClick(Event event, ImageButton hasFailedButton, boolean hasPressedFailedButton, String errorMessage, int status)
    {
        this.event = event;
        this.hasFailedButton = hasFailedButton;
        this.hasPressedFailedButton = hasPressedFailedButton;
        this.errorMessage = errorMessage;
        this.status = status;
    }
    public boolean isHasPressedFailedButton() {
        return hasPressedFailedButton;
    }

    public ImageButton getHasFailedButton() {
        return hasFailedButton;
    }

    public Event getEvent() {
        return event;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public int getStatus() {
        return status;
    }
}
