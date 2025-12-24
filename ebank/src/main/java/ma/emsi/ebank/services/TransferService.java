package ma.emsi.ebank.services;

import ma.emsi.ebank.dto.transfer.TransferRequest;

public interface TransferService {
    void transfer(TransferRequest request);
}
