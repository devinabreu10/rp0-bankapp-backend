package dev.abreu.bankapp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import dev.abreu.bankapp.dao.TransferDao;
import dev.abreu.bankapp.entity.Transfer;
import static dev.abreu.bankapp.util.BankappConstants.SQL_EXCEPTION_CAUGHT;
import static dev.abreu.bankapp.util.BankappQueryConstants.SELECT_TRANSFERS_BY_ID_QUERY;

@Repository
public class TransferDaoImpl implements TransferDao {
	
	private static final Logger log = LogManager.getLogger(TransferDaoImpl.class);

	private static final String TRANSFER_ID = "transfer_id";
	private static final String SOURCE_ACCT_NUM = "source_acct_num";
	private static final String TARGET_ACCT_NUM = "target_acct_num";
	private static final String TRANSFER_AMOUNT = "transfer_amount";
	private static final String TRANSFER_NOTES = "transfer_notes";
	private static final String CREATED_AT = "created_at";

	private final DataSource dataSource;

	public TransferDaoImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Optional<Transfer> findTransferById(Long transferId) {
		Transfer transfer = new Transfer();
		
		try(Connection conn = dataSource.getConnection();
				PreparedStatement prepStmt = conn.prepareStatement(SELECT_TRANSFERS_BY_ID_QUERY)) {
			
			prepStmt.setLong(1, transferId);
			
			ResultSet resultSet = prepStmt.executeQuery();
			
			if(resultSet.next()) {
				setTransferFromResultSet(transfer, resultSet);
			} else {
				return Optional.empty();
			}
			
		} catch (SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT + "findTransferById: {}", e.getMessage());
			return Optional.empty();
		}
		
		return Optional.of(transfer);
	}

	/**
	 * Helper method to set Transfer object fields from ResultSet
	 *
	 * @param transfer the Transfer object to populate
	 * @param resultSet the ResultSet containing transfer data
	 * @throws SQLException if there's an error accessing the ResultSet
	 */
	private void setTransferFromResultSet(Transfer transfer, ResultSet resultSet) throws SQLException {
		transfer.setTransferId(resultSet.getLong(TRANSFER_ID));
		transfer.setSourceAccountNumber(resultSet.getLong(SOURCE_ACCT_NUM));
		transfer.setTargetAccountNumber(resultSet.getLong(TARGET_ACCT_NUM));
		transfer.setTransferAmount(resultSet.getDouble(TRANSFER_AMOUNT));
		transfer.setTransferNotes(resultSet.getString(TRANSFER_NOTES));
		transfer.setCreatedAt(resultSet.getTimestamp(CREATED_AT).toLocalDateTime());
	}

}