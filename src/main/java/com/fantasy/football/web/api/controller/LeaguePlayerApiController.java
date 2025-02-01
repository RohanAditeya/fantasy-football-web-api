package com.fantasy.football.web.api.controller;

import com.fantasy.football.dto.CreateLeaguePlayerRequest;
import com.fantasy.football.dto.PlayerBasicInformationPatchDTO;
import com.fantasy.football.model.PlayerBasicInformation;
import com.fantasy.football.reactive.controller.LeaguePlayerApi;
import com.fantasy.football.web.api.service.PlayerBasicInformationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class LeaguePlayerApiController implements LeaguePlayerApi {

    private final PlayerBasicInformationService playerBasicInformationService;

    @Override
    public Mono<ResponseEntity<PlayerBasicInformation>> createLeaguePlayer(@Valid Mono<CreateLeaguePlayerRequest> createLeaguePlayerRequest, ServerWebExchange exchange) {
        return playerBasicInformationService.validateAndSaveLeaguePlayer(createLeaguePlayerRequest)
                .doOnSuccess(playerBasicInformation -> log.info("Saved player {} successfully", playerBasicInformation.getRecordId()))
                .map(savedResponse -> ResponseEntity.status(HttpStatus.CREATED).body(savedResponse));
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteLeaguePlayer(UUID recordId, String playerCode, ServerWebExchange exchange) {
        // TODO Change player code header to accept long
        // TODO return 400 when no headers are provided
        return playerBasicInformationService.deleteLeaguePlayerBasicInfoRecord(recordId, playerCode != null ? Long.valueOf(playerCode) : null).thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @Override
    public Mono<ResponseEntity<Flux<PlayerBasicInformation>>> fetchLeaguePlayer(UUID recordId, String playerCode, String teamId, @Valid Integer pageNumber, ServerWebExchange exchange) {
        // TODO Update model to accept page size parameter
        // TODO Change player code header to accept long
        // TODO Change teamId header to accept UUID
        // TODO return 400 when no headers are provided
        // TODO return 404 when no record is found
        return Mono.just(ResponseEntity.ok(playerBasicInformationService.fetchLeaguePlayer(recordId, playerCode != null ? Long.valueOf(playerCode) : null, teamId, pageNumber)));
    }

    @Override
    public Mono<ResponseEntity<PlayerBasicInformation>> updateLeaguePlayer(@Valid Mono<PlayerBasicInformationPatchDTO> playerBasicInformationPatchDTO, UUID recordId, String playerCode, ServerWebExchange exchange) {
        // TODO Do we need to change Player Basic Information status field to char instead of string?
        // TODO First name and Second name is immutable in entity and patch request accepts values for it. Either entity must be made mutable or path request entity must stop accepting first and second name.
        // TODO return 404 when no record is found to update
        // TODO return 400 when no headers are provided
        return playerBasicInformationPatchDTO
                .flatMap(patchRequest -> playerBasicInformationService.updatePlayerBasicInformation(patchRequest, recordId, playerCode != null? Long.valueOf(playerCode) : null))
                .map(updatedRecord -> ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedRecord))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.ACCEPTED).build()));
    }
}