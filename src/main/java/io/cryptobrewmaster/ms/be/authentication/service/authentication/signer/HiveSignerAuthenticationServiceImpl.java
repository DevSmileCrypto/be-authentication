package io.cryptobrewmaster.ms.be.authentication.service.authentication.signer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.view.RedirectView;

@RequiredArgsConstructor
@Service
public class HiveSignerAuthenticationServiceImpl implements HiveSignerAuthenticationService {

    @Override
    public RedirectView generateLoginUrl() {
        return new RedirectView();
    }

    @Override
    public RedirectView completeRegistrationOrLogin(MultiValueMap<String, String> params) {
        return new RedirectView();
    }

}
