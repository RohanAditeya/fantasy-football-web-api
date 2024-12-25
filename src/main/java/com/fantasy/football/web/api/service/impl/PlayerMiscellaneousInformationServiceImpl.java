package com.fantasy.football.web.api.service.impl;

import com.fantasy.football.model.PlayerMiscellaneousInformation;
import com.fantasy.football.web.api.repository.PlayerMiscellaneousInformationRepository;
import com.fantasy.football.web.api.service.PlayerMiscellaneousInformationService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class PlayerMiscellaneousInformationServiceImpl implements PlayerMiscellaneousInformationService {

    private final PlayerMiscellaneousInformationRepository playerMiscellaneousInformationRepository;

    @Override
    public Mono<PlayerMiscellaneousInformation> validateAndSavePlayerMiscellaneousInformation(Mono<PlayerMiscellaneousInformation> playerMiscellaneousInformation) {
        return playerMiscellaneousInformation
                .flatMap(playerMiscellaneousInformationRepository::save);
    }
}