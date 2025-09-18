package com.goodtricount.dto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Group entities.
 * Handles database operations for groups.
 */
public class GroupDAO {
    
    /**
     * Insert a new group into the database.
     * 
     * @param group the group to insert
     * @return true if the group was inserted successfully, false otherwise
     */
    public boolean insertGroup(GroupDTO group) {
        String sql = "INSERT INTO Groups (id, name) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, group.getId());
            pstmt.setString(2, group.getName());
            
            int rowsAffected = pstmt.executeUpdate();
            
            // If the group was inserted successfully, insert participants and admins
            if (rowsAffected > 0) {
                insertParticipants(group.getId(), group.getParticipants(), conn);
                insertAdmins(group.getId(), group.getAdmins(), conn);
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            System.out.println("Error inserting group: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Insert participants for a group.
     * 
     * @param groupId the ID of the group
     * @param participants the list of participant usernames
     * @param conn the database connection
     * @throws SQLException if a database access error occurs
     */
    private void insertParticipants(String groupId, List<String> participants, Connection conn) throws SQLException {
        String sql = "INSERT INTO GroupParticipants (group_id, user_id) VALUES (?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (String participant : participants) {
                pstmt.setString(1, groupId);
                pstmt.setString(2, participant);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }
    
    /**
     * Insert admins for a group.
     * 
     * @param groupId the ID of the group
     * @param admins the list of admin usernames
     * @param conn the database connection
     * @throws SQLException if a database access error occurs
     */
    private void insertAdmins(String groupId, List<String> admins, Connection conn) throws SQLException {
        String sql = "INSERT INTO GroupAdmins (group_id, admin_id) VALUES (?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (String admin : admins) {
                pstmt.setString(1, groupId);
                pstmt.setString(2, admin);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }
    
    /**
     * Get a group by ID.
     * 
     * @param groupId the ID of the group to get
     * @return the group, or null if not found
     */
    public GroupDTO getGroupById(String groupId) {
        String sql = "SELECT * FROM Groups WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, groupId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    GroupDTO group = new GroupDTO();
                    group.setId(rs.getString("id"));
                    group.setName(rs.getString("name"));
                    
                    // Get participants
                    group.setParticipants(getParticipants(groupId, conn));
                    
                    // Get admins
                    group.setAdmins(getAdmins(groupId, conn));
                    
                    // Get expenses (would be implemented in ExpenseDAO)
                    // Get payments (would be implemented in PaymentDAO)
                    
                    return group;
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error getting group: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get participants for a group.
     * 
     * @param groupId the ID of the group
     * @param conn the database connection
     * @return the list of participant usernames
     * @throws SQLException if a database access error occurs
     */
    private List<String> getParticipants(String groupId, Connection conn) throws SQLException {
        List<String> participants = new ArrayList<>();
        String sql = "SELECT user_id FROM GroupParticipants WHERE group_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, groupId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    participants.add(rs.getString("user_id"));
                }
            }
        }
        
        return participants;
    }
    
    /**
     * Get admins for a group.
     * 
     * @param groupId the ID of the group
     * @param conn the database connection
     * @return the list of admin usernames
     * @throws SQLException if a database access error occurs
     */
    private List<String> getAdmins(String groupId, Connection conn) throws SQLException {
        List<String> admins = new ArrayList<>();
        String sql = "SELECT admin_id FROM GroupAdmins WHERE group_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, groupId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    admins.add(rs.getString("admin_id"));
                }
            }
        }
        
        return admins;
    }
    
    /**
     * Get all groups from the database.
     * 
     * @return a list of all groups
     */
    public List<GroupDTO> getAllGroups() {
        List<GroupDTO> groups = new ArrayList<>();
        String sql = "SELECT * FROM Groups";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                String groupId = rs.getString("id");
                
                GroupDTO group = new GroupDTO();
                group.setId(groupId);
                group.setName(rs.getString("name"));
                
                // Get participants
                group.setParticipants(getParticipants(groupId, conn));
                
                // Get admins
                group.setAdmins(getAdmins(groupId, conn));
                
                // Get expenses (would be implemented in ExpenseDAO)
                // Get payments (would be implemented in PaymentDAO)
                
                groups.add(group);
            }
            
        } catch (SQLException e) {
            System.out.println("Error getting all groups: " + e.getMessage());
            e.printStackTrace();
        }
        
        return groups;
    }
    
    /**
     * Get groups for a user.
     * 
     * @param username the username of the user
     * @return a list of groups the user is a participant in
     */
    public List<GroupDTO> getGroupsForUser(String username) {
        List<GroupDTO> groups = new ArrayList<>();
        String sql = "SELECT g.* FROM Groups g " +
                     "JOIN GroupParticipants gp ON g.id = gp.group_id " +
                     "WHERE gp.user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String groupId = rs.getString("id");
                    
                    GroupDTO group = new GroupDTO();
                    group.setId(groupId);
                    group.setName(rs.getString("name"));
                    
                    // Get participants
                    group.setParticipants(getParticipants(groupId, conn));
                    
                    // Get admins
                    group.setAdmins(getAdmins(groupId, conn));
                    
                    // Get expenses (would be implemented in ExpenseDAO)
                    // Get payments (would be implemented in PaymentDAO)
                    
                    groups.add(group);
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error getting groups for user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return groups;
    }
    
    /**
     * Update a group in the database.
     * 
     * @param group the group to update
     * @return true if the group was updated successfully, false otherwise
     */
    public boolean updateGroup(GroupDTO group) {
        String sql = "UPDATE Groups SET name = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, group.getName());
            pstmt.setString(2, group.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            
            // If the group was updated successfully, update participants and admins
            if (rowsAffected > 0) {
                // Delete existing participants and admins
                deleteParticipants(group.getId(), conn);
                deleteAdmins(group.getId(), conn);
                
                // Insert new participants and admins
                insertParticipants(group.getId(), group.getParticipants(), conn);
                insertAdmins(group.getId(), group.getAdmins(), conn);
                
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            System.out.println("Error updating group: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete participants for a group.
     * 
     * @param groupId the ID of the group
     * @param conn the database connection
     * @throws SQLException if a database access error occurs
     */
    private void deleteParticipants(String groupId, Connection conn) throws SQLException {
        String sql = "DELETE FROM GroupParticipants WHERE group_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, groupId);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Delete admins for a group.
     * 
     * @param groupId the ID of the group
     * @param conn the database connection
     * @throws SQLException if a database access error occurs
     */
    private void deleteAdmins(String groupId, Connection conn) throws SQLException {
        String sql = "DELETE FROM GroupAdmins WHERE group_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, groupId);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Delete a group from the database.
     * 
     * @param groupId the ID of the group to delete
     * @return true if the group was deleted successfully, false otherwise
     */
    public boolean deleteGroup(String groupId) {
        String sql = "DELETE FROM Groups WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, groupId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error deleting group: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Add a participant to a group.
     * 
     * @param groupId the ID of the group
     * @param username the username of the participant to add
     * @return true if the participant was added successfully, false otherwise
     */
    public boolean addParticipant(String groupId, String username) {
        String sql = "INSERT INTO GroupParticipants (group_id, user_id) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, groupId);
            pstmt.setString(2, username);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error adding participant: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Add an admin to a group.
     * 
     * @param groupId the ID of the group
     * @param username the username of the admin to add
     * @return true if the admin was added successfully, false otherwise
     */
    public boolean addAdmin(String groupId, String username) {
        // First, ensure the user is a participant
        if (!isParticipant(groupId, username)) {
            addParticipant(groupId, username);
        }
        
        String sql = "INSERT INTO GroupAdmins (group_id, admin_id) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, groupId);
            pstmt.setString(2, username);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error adding admin: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Check if a user is a participant in a group.
     * 
     * @param groupId the ID of the group
     * @param username the username of the user
     * @return true if the user is a participant, false otherwise
     */
    public boolean isParticipant(String groupId, String username) {
        String sql = "SELECT COUNT(*) FROM GroupParticipants WHERE group_id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, groupId);
            pstmt.setString(2, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error checking if user is participant: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Check if a user is an admin in a group.
     * 
     * @param groupId the ID of the group
     * @param username the username of the user
     * @return true if the user is an admin, false otherwise
     */
    public boolean isAdmin(String groupId, String username) {
        String sql = "SELECT COUNT(*) FROM GroupAdmins WHERE group_id = ? AND admin_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, groupId);
            pstmt.setString(2, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error checking if user is admin: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
}