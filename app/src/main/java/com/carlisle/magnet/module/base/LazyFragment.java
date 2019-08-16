package com.carlisle.magnet.module.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class LazyFragment extends BaseFragment {
    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;
    private boolean isVisibleToUser = false;
    private List<FragmentVisibleListener> fragmentVisibleListeners = new ArrayList();
    private LazyFragment.FragmentVisibleListener listener = new LazyFragment.FragmentVisibleListener() {
        public void onVisibleChanged(boolean isVisible) {
            LazyFragment.this.setVisibleToUser(isVisible);
        }
    };

    public LazyFragment() {
    }

    final void addVisibleListener(LazyFragment.FragmentVisibleListener listener) {
        if (!this.fragmentVisibleListeners.contains(listener)) {
            this.fragmentVisibleListeners.add(listener);
        }

    }

    final void removeVisibleListener(LazyFragment.FragmentVisibleListener listener) {
        this.fragmentVisibleListeners.remove(listener);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.isFirstVisible = true;
        this.isFirstInvisible = true;
        this.isVisibleToUser = false;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initParentVisibleListener();
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.removeParentVisibleListener();
    }

    private void initParentVisibleListener() {
        Fragment fragment = this.getParentFragment();
        if (fragment != null && fragment instanceof LazyFragment) {
            ((LazyFragment) fragment).addVisibleListener(this.listener);
        }

    }

    private void removeParentVisibleListener() {
        Fragment fragment = this.getParentFragment();
        if (fragment != null && fragment instanceof LazyFragment) {
            ((LazyFragment) fragment).removeVisibleListener(this.listener);
        }

    }

    public void onResume() {
        super.onResume();
        this.setVisibleToUser(true);
    }

    public void onPause() {
        super.onPause();
        this.setVisibleToUser(false);
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.setVisibleToUser(isVisibleToUser);
    }

    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.setVisibleToUser(!hidden);
    }

    private boolean checkVisible() {
        for (Fragment fragment = this.getParentFragment(); fragment != null; fragment = fragment.getParentFragment()) {
            if (fragment.isHidden() || !fragment.getUserVisibleHint()) {
                return false;
            }
        }

        return !this.isHidden() && this.getUserVisibleHint();
    }

    private void notifyFragmentVisibleListeners(boolean isVisible) {
        Iterator var2 = this.fragmentVisibleListeners.iterator();

        while (var2.hasNext()) {
            LazyFragment.FragmentVisibleListener listener = (LazyFragment.FragmentVisibleListener) var2.next();
            listener.onVisibleChanged(isVisible);
        }

    }

    protected void onFirstUserVisible() {
        this.onUserVisible();
    }

    protected void onUserVisible() {
    }

    protected void onFirstUserInvisible() {
        this.onUserInvisible();
    }

    protected void onUserInvisible() {
    }

    public boolean isVisibleToUser() {
        return this.isVisibleToUser;
    }

    public void setVisibleToUser(boolean isVisibleToUser) {
        if (this.getView() != null) {
            if (isVisibleToUser) {
                if (!this.isVisibleToUser && this.checkVisible()) {
                    if (this.isFirstVisible) {
                        this.isFirstVisible = false;
                        this.onFirstUserVisible();
                    } else {
                        this.onUserVisible();
                    }

                    this.isVisibleToUser = isVisibleToUser;
                    this.notifyFragmentVisibleListeners(isVisibleToUser);
                }
            } else {
                if (this.isVisibleToUser) {
                    if (this.isFirstInvisible) {
                        this.isFirstInvisible = false;
                        this.onFirstUserInvisible();
                    } else {
                        this.onUserInvisible();
                    }

                    this.notifyFragmentVisibleListeners(isVisibleToUser);
                }

                this.isVisibleToUser = isVisibleToUser;
            }
        }

    }

    private interface FragmentVisibleListener {
        void onVisibleChanged(boolean var1);
    }
}
