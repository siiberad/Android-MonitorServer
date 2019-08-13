package co.id.vostra.metric.event;

public class Ringtone {
   private boolean notif;

    public Ringtone(boolean notif) {
        this.notif = notif;
    }

    public boolean isNotif() {
        return notif;
    }

    public void setNotif(boolean notif) {
        this.notif = notif;
    }
}
