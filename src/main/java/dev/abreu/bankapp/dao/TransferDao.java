package dev.abreu.bankapp.dao;

import java.util.Optional;

import dev.abreu.bankapp.entity.Transfer;

/**
 * The TransferDao interface provides methods for interacting with the
 * Transfer model in the database. It is responsible for finding, saving,
 * updating, and deleting Transfer objects.
 * 
 * @author Devin Abreu
 *
 */
public interface TransferDao {

	/**
	 * Finds a Transfer by transfer ID.
	 *
	 * @param transferId the ID of the Transfer to search for
	 * @return an Optional containing the Transfer if found, or an empty Optional if not found
	 */
	Optional<Transfer> findTransferById(Long transferId);

}