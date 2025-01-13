package com.fantasy.football.web.api.controller;

import com.fantasy.football.dto.PlayerMiscellaneousInformationPatchDTO;
import com.fantasy.football.model.PlayerMiscellaneousInformation;
import com.fantasy.football.reactive.controller.LeaguePlayerMiscStatisticsApi;
import com.fantasy.football.web.api.service.PlayerMiscellaneousInformationService;
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
public class PlayerMiscStatisticsApiController implements LeaguePlayerMiscStatisticsApi {

    private final PlayerMiscellaneousInformationService miscellaneousInformationService;

    @Override
    public Mono<ResponseEntity<PlayerMiscellaneousInformation>> createMiscellaneousStatistics(@Valid Mono<PlayerMiscellaneousInformation> body, ServerWebExchange exchange) {
        Mono<PlayerMiscellaneousInformation> requestWithIdGenerated = body.doOnNext(entity -> entity.setRecordId(UUID.randomUUID()));
        return miscellaneousInformationService.validateAndSavePlayerMiscellaneousInformation(requestWithIdGenerated)
                .doOnNext(savedRecord -> log.info("Saved league Player misc statistics record with ID {}", savedRecord.getRecordId()))
                .map(savedRecord -> ResponseEntity.status(HttpStatus.CREATED).body(savedRecord));
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteMiscellaneousStatistics(UUID recordId, UUID playerId, ServerWebExchange exchange) {
        // TODO playerId can be removed from header.
        // TODO return 400 when record_id header is missing
        return miscellaneousInformationService.deleteMiscellaneousInformationRecordById(recordId)
                .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build()))
                .doOnSuccess(voidType -> log.info("Deleted league player misc statistics record with ID {}", recordId));
    }

    @Override
    public Mono<ResponseEntity<Flux<PlayerMiscellaneousInformation>>> fetchMiscellaneousStatistics(UUID recordId, UUID playerId, @Valid Integer pageNumber, ServerWebExchange exchange) {
        // TODO Remove page number and player ID. No longer need those headers.
        // TODO return 404 when record is not found
        // TODO return 400 when record_id header is missing
        return Mono.just(ResponseEntity.ok(miscellaneousInformationService.fetchPlayerMiscRecord(recordId)));
    }

    @Override
    public Mono<ResponseEntity<PlayerMiscellaneousInformation>> updateMiscellaneousStatistics(@Valid Mono<PlayerMiscellaneousInformationPatchDTO> playerMiscellaneousInformationPatchDTO, UUID recordId, UUID playerId, ServerWebExchange exchange) {
        // TODO Remove player ID from header.
        // TODO 404 when record to update is not found
        return playerMiscellaneousInformationPatchDTO
                .flatMap(updateRequest -> miscellaneousInformationService.updateMiscStatisticsInformation(updateRequest, recordId))
                .map(updatedRecord -> ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedRecord))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.ACCEPTED).build()));
    }
}