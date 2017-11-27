package com.gxuc.runfast.business.ui;

import com.gxuc.runfast.business.data.repo.LoginRepo;

class CheckLoginState {

    static boolean hasLoggedIn() {
        return LoginRepo.get().hasLoggedIn();
    }
}
