package com.fantasy.football.web.api.leagueteam.service.impl;

import com.fantasy.football.dto.LeagueTeamPatchDto;
import com.fantasy.football.model.LeagueTeam;
import com.fantasy.football.web.api.common.exception.BadInputException;
import com.fantasy.football.web.api.common.exception.RecordNotFoundException;
import com.fantasy.football.web.api.leagueteam.repository.LeagueTeamRepository;
import com.fantasy.football.web.api.leagueteam.service.LeagueTeamApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
class LeagueTeamApiServiceImpl implements LeagueTeamApiService {

    private final LeagueTeamRepository leagueTeamRepository;

    LeagueTeamApiServiceImpl(LeagueTeamRepository leagueTeamRepository) {
        this.leagueTeamRepository = leagueTeamRepository;
    }

    @Override
    public Mono<LeagueTeam> validateAndSaveLeagueTeam(Mono<LeagueTeam> requestBody) {
        return requestBody
                .doOnNext(leagueTeamRecord -> leagueTeamRecord.setRecordId(UUID.randomUUID()))
                .flatMap(leagueTeamRepository::save)
                .doOnSuccess(savedRecord -> log.info("Saved League team record successfully"));
    }

    @Override
    public Mono<Void> deleteLeagueTeamRecord(String teamName, Integer teamCode, UUID recordId) {
        Mono<Void> returnPipeline;
        if (recordId != null)
            returnPipeline = leagueTeamRepository.deleteById(recordId);
        else if (teamCode != null)
            returnPipeline = leagueTeamRepository.deleteByCode(teamCode);
        else if (teamName != null)
            returnPipeline = leagueTeamRepository.deleteByName(teamName);
        else
            throw new BadInputException("Request needs to provided with at least one of [teamName, teamCode, recordId] headers", HttpStatus.BAD_REQUEST.value());
        return returnPipeline.doOnSuccess((voidElement) -> log.info("Deleted League team record successfully"));
    }

    @Override
    public Mono<LeagueTeam> updateLeagueTeamRecord(String teamName, Integer teamCode, UUID recordId, Mono<LeagueTeamPatchDto> leagueTeamPatchDto) {
        return leagueTeamPatchDto
                .flatMap(updateRequest -> this.fetchAndUpdateLeagueTeamRecord(teamName, teamCode, recordId, updateRequest))
                .doOnSuccess(updatedRecord -> log.info("Updated League team record successfully"));
    }

    @Override
    public Flux<LeagueTeam> fetchLeagueTeamRecords(String teamName, Integer teamCode, UUID recordId, Integer pageNumber, Integer pageSize) {
        Flux<LeagueTeam> fetchedRecords;
        if (recordId != null)
            fetchedRecords = leagueTeamRepository.findById(recordId).flux();
        else if (teamName != null)
            fetchedRecords = leagueTeamRepository.findByName(teamName);
        else if (teamCode != null)
            fetchedRecords = leagueTeamRepository.findByCode(teamCode).flux();
        else
            fetchedRecords = leagueTeamRepository.findAll(Sort.sort(LeagueTeam.class).by(LeagueTeam::getCode).ascending())
                    .skip((pageNumber - 1L) * pageSize).take(pageSize);
        return fetchedRecords
                .switchIfEmpty(Flux.error(new RecordNotFoundException("No league team record found matching the request criteria", HttpStatus.NOT_FOUND.value())))
                .doOnComplete(() -> log.info("Fetched League team records successfully"));
    }

    private Mono<LeagueTeam> fetchAndUpdateLeagueTeamRecord(String teamName, Integer teamCode, UUID recordId, LeagueTeamPatchDto leagueTeamPatchDto) {
        return fetchLeagueTeamRecords(teamName, teamCode, recordId, 1, 1).elementAt(0)
                .map(
                        fetchedRecord -> {
                            Optional.ofNullable(leagueTeamPatchDto.getDraw()).ifPresent(fetchedRecord::setDraw);
                            Optional.ofNullable(leagueTeamPatchDto.getForm()).ifPresent(fetchedRecord::setForm);
                            Optional.ofNullable(leagueTeamPatchDto.getLoss()).ifPresent(fetchedRecord::setLoss);
                            Optional.ofNullable(leagueTeamPatchDto.getPlayed()).ifPresent(fetchedRecord::setPlayed);
                            Optional.ofNullable(leagueTeamPatchDto.getPoints()).ifPresent(fetchedRecord::setPoints);
                            Optional.ofNullable(leagueTeamPatchDto.getPosition()).ifPresent(fetchedRecord::setPosition);
                            Optional.ofNullable(leagueTeamPatchDto.getShortName()).ifPresent(fetchedRecord::setShortName);
                            Optional.ofNullable(leagueTeamPatchDto.getStrength()).ifPresent(fetchedRecord::setStrength);
                            Optional.ofNullable(leagueTeamPatchDto.getTeamDivision()).ifPresent(fetchedRecord::setTeamDivision);
                            Optional.ofNullable(leagueTeamPatchDto.getUnavailable()).ifPresent(fetchedRecord::setUnavailable);
                            Optional.ofNullable(leagueTeamPatchDto.getWin()).ifPresent(fetchedRecord::setWin);
                            Optional.ofNullable(leagueTeamPatchDto.getStrengthOverallHome()).ifPresent(fetchedRecord::setStrengthOverallHome);
                            Optional.ofNullable(leagueTeamPatchDto.getStrengthOverallAway()).ifPresent(fetchedRecord::setStrengthOverallAway);
                            Optional.ofNullable(leagueTeamPatchDto.getStrengthAttackHome()).ifPresent(fetchedRecord::setStrengthAttackHome);
                            Optional.ofNullable(leagueTeamPatchDto.getStrengthAttackAway()).ifPresent(fetchedRecord::setStrengthAttackAway);
                            Optional.ofNullable(leagueTeamPatchDto.getStrengthDefenceHome()).ifPresent(fetchedRecord::setStrengthDefenceHome);
                            Optional.ofNullable(leagueTeamPatchDto.getStrengthDefenceAway()).ifPresent(fetchedRecord::setStrengthDefenceAway);
                            Optional.ofNullable(leagueTeamPatchDto.getPulseId()).ifPresent(fetchedRecord::setPulseId);

                            return fetchedRecord;
                        }
                ).flatMap(leagueTeamRepository::save);
    }
}