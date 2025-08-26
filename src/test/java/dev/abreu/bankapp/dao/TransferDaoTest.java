package dev.abreu.bankapp.dao;

import dev.abreu.bankapp.dao.impl.TransferDaoImpl;
import dev.abreu.bankapp.entity.Transfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferDaoTest {
	
	@Mock
	private DataSource dataSourceMock;

	@Mock
	private Connection connectionMock;

	@Mock
	private PreparedStatement preparedStatementMock;

	@Mock
	private ResultSet resultSetMock;

    @InjectMocks
	private TransferDaoImpl transferDao;

	@BeforeEach
	void setup() throws SQLException {
		when(dataSourceMock.getConnection()).thenReturn(connectionMock);
	}

    @Test
    void testFindTransferById() throws SQLException {
        Long transferId = 1L;
        Transfer expectedTransfer = new Transfer(12345L, 67890L, 100.00, "Transfer notes");
        LocalDateTime testDateTime = LocalDateTime.of(2024, 4, 24, 15, 30);
        expectedTransfer.setTransferId(transferId);
        expectedTransfer.setCreatedAt(testDateTime);

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getLong("transfer_id")).thenReturn(1L);
		when(resultSetMock.getLong("source_acct_num")).thenReturn(12345L);
		when(resultSetMock.getLong("target_acct_num")).thenReturn(67890L);
		when(resultSetMock.getDouble("transfer_amount")).thenReturn(100.00);
		when(resultSetMock.getString("transfer_notes")).thenReturn("Transfer notes");
		when(resultSetMock.getTimestamp("created_at")).thenReturn(Timestamp.valueOf(testDateTime));

        Optional<Transfer> result = transferDao.findTransferById(transferId);

        assertTrue(result.isPresent());
        assertEquals(expectedTransfer.getTransferId(), result.get().getTransferId());
        assertEquals(expectedTransfer.getSourceAccountNumber(), result.get().getSourceAccountNumber());
        assertEquals(expectedTransfer.getTargetAccountNumber(), result.get().getTargetAccountNumber());
        assertEquals(expectedTransfer.getTransferAmount(), result.get().getTransferAmount());
        assertEquals(expectedTransfer.getTransferNotes(), result.get().getTransferNotes());
        assertEquals(expectedTransfer.getCreatedAt(), result.get().getCreatedAt());
    }
  
    @Test
    void testFindTransferByIdEmptyOptional() throws SQLException {
        Long transferId = 1L;

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(false);

        Optional<Transfer> result = transferDao.findTransferById(transferId);

        assertEquals(Optional.empty(), result);
    }

    @Test
    void testFindTransferByIdSQLException() throws SQLException {
        Long transferId = 1L;

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenThrow(SQLException.class);

        Optional<Transfer> result = transferDao.findTransferById(transferId);

        assertEquals(Optional.empty(), result);
    }



}