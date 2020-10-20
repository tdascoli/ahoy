package com.apollo29.ahoy.comm;


import com.apollo29.ahoy.comm.profile.Profile;
import com.apollo29.ahoy.comm.profile.ProfileService;

import io.reactivex.rxjava3.core.Single;

public class ProfileRepository {

    public static Single<Profile> putProfile(Profile profile){
        ProfileService service = RetrofitClientInstance.getRetrofitInstance().create(ProfileService.class);
        return service.putProfile(profile);
    }
}
