package org.example.dao;

import org.example.model.UserProfile;

public interface UserProfileDAO {
    boolean addProfile(UserProfile p);

    boolean updateProfile(UserProfile p);
}
