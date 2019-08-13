package co.id.vostra.metric.adapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import co.id.vostra.metric.model.ServerModel;
import co.id.vostra.metric.model.Services;

public class ServerDiff extends DiffUtil.Callback {

    private final List<ServerModel> mOldServerList;
    private final List<ServerModel> mNewServerList;
//    private final List<Services> mOldServicesList;
//    private final List<Services> mNewServicesList;

    public ServerDiff(List<ServerModel> oldServerList, List<ServerModel> newServerList) {
        this.mOldServerList = oldServerList;
        this.mNewServerList = newServerList;
    }

    @Override
    public int getOldListSize() {
        return mOldServerList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewServerList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldServerList.get(oldItemPosition).getName().equals(mNewServerList.get(
                newItemPosition).getName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final ServerModel oldServer = mOldServerList.get(oldItemPosition);
        final ServerModel newServer = mNewServerList.get(newItemPosition);

        return oldServer.getName().equals(newServer.getName());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}