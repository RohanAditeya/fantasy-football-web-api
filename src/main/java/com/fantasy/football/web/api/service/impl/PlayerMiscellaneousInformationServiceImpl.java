package com.fantasy.football.web.api.service.impl;

import com.fantasy.football.dto.PlayerMiscellaneousInformationPatchDTO;
import com.fantasy.football.model.PlayerMiscellaneousInformation;
import com.fantasy.football.web.api.repository.PlayerMiscellaneousInformationRepository;
import com.fantasy.football.web.api.service.PlayerMiscellaneousInformationService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class PlayerMiscellaneousInformationServiceImpl implements PlayerMiscellaneousInformationService {

    private final PlayerMiscellaneousInformationRepository playerMiscellaneousInformationRepository;

    @Override
    public Mono<PlayerMiscellaneousInformation> validateAndSavePlayerMiscellaneousInformation(Mono<PlayerMiscellaneousInformation> playerMiscellaneousInformation) {
        return playerMiscellaneousInformation
                .flatMap(playerMiscellaneousInformationRepository::save);
    }

    @Override
    public Mono<Void> deleteMiscellaneousInformationRecordById(UUID recordId) {
        return playerMiscellaneousInformationRepository.deleteById(recordId);
    }

    @Override
    public Flux<PlayerMiscellaneousInformation> fetchPlayerMiscRecord(UUID recordId) {
        return playerMiscellaneousInformationRepository.findById(recordId)
                .doOnNext(fetchedRecord -> log.info("Fetched player fantasy statistics record with ID {}", recordId))
                .flux().switchIfEmpty(Flux.<PlayerMiscellaneousInformation>empty().doOnComplete(() -> log.info("Found no player fantasy statistics record with ID {}", recordId)));
    }

    @Override
    public Mono<PlayerMiscellaneousInformation> updateMiscStatisticsInformation(PlayerMiscellaneousInformationPatchDTO updateDto, UUID recordId) {
        return playerMiscellaneousInformationRepository.findById(recordId).doOnNext(
                        fetchedRecord -> {
                            Optional.ofNullable(updateDto.getNewsAdded()).ifPresent(fetchedRecord::setNewsAdded);
                            Optional.ofNullable(updateDto.getIctIndex()).ifPresent(fetchedRecord::setIctIndex);
                            Optional.ofNullable(updateDto.getInfluenceRank()).ifPresent(fetchedRecord::setInfluenceRank);
                            Optional.ofNullable(updateDto.getInfluenceRankType()).ifPresent(fetchedRecord::setInfluenceRankType);
                            Optional.ofNullable(updateDto.getCreativityRank()).ifPresent(fetchedRecord::setCreativityRank);
                            Optional.ofNullable(updateDto.getCreativityRankType()).ifPresent(fetchedRecord::setCreativityRankType);
                            Optional.ofNullable(updateDto.getThreatRank()).ifPresent(fetchedRecord::setThreatRank);
                            Optional.ofNullable(updateDto.getThreatRankType()).ifPresent(fetchedRecord::setThreatRankType);
                            Optional.ofNullable(updateDto.getIctIndexRank()).ifPresent(fetchedRecord::setIctIndexRank);
                            Optional.ofNullable(updateDto.getIctIndexRankType()).ifPresent(fetchedRecord::setIctIndexRankType);
                            Optional.ofNullable(updateDto.getCornersAndIndirectFreeKicksOrder()).ifPresent(fetchedRecord::setCornersAndIndirectFreeKicksOrder);
                            Optional.ofNullable(updateDto.getCornersAndIndirectFreeKicksText()).ifPresent(fetchedRecord::setCornersAndIndirectFreeKicksText);
                            Optional.ofNullable(updateDto.getDirectFreeKicksOrder()).ifPresent(fetchedRecord::setDirectFreeKicksOrder);
                            Optional.ofNullable(updateDto.getDirectFreeKicksText()).ifPresent(fetchedRecord::setDirectFreeKicksText);
                            Optional.ofNullable(updateDto.getPenaltiesOrder()).ifPresent(fetchedRecord::setPenaltiesOrder);
                            Optional.ofNullable(updateDto.getPenaltiesText()).ifPresent(fetchedRecord::setPenaltiesText);
                            Optional.ofNullable(updateDto.getNowCostRank()).ifPresent(fetchedRecord::setNowCostRank);
                            Optional.ofNullable(updateDto.getNowCostRankType()).ifPresent(fetchedRecord::setNowCostRankType);
                            Optional.ofNullable(updateDto.getFormRank()).ifPresent(fetchedRecord::setFormRank);
                            Optional.ofNullable(updateDto.getFormRankType()).ifPresent(fetchedRecord::setFormRankType);
                            Optional.ofNullable(updateDto.getPointsPerGameRank()).ifPresent(fetchedRecord::setPointsPerGameRank);
                            Optional.ofNullable(updateDto.getPointsPerGameRankType()).ifPresent(fetchedRecord::setPointsPerGameRankType);
                            Optional.ofNullable(updateDto.getSelectedRank()).ifPresent(fetchedRecord::setSelectedRank);
                            Optional.ofNullable(updateDto.getSelectedRankType()).ifPresent(fetchedRecord::setSelectedRankType);
                        }
                )
                .flatMap(playerMiscellaneousInformationRepository::save)
                .doOnNext(updatedRecord -> log.info("Updated player misc statistics record with ID {}", updatedRecord.getRecordId()))
                .switchIfEmpty(Mono.<PlayerMiscellaneousInformation>empty().doOnSuccess(voidType -> log.info("Cannot find any player misc statistics record with ID {} for updating", recordId)));
    }
}