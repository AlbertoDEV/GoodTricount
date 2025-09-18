package com.goodtricount.api;

import com.goodtricount.dto.UserDAO;
import com.goodtricount.dto.UserDTO;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDTO loginUser = gson.fromJson(req.getReader(), UserDTO.class);

        UserDTO user = userDAO.getUserByUsername(loginUser.getUsername());

        if (user != null && user.getPassword().equals(loginUser.getPassword())) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            // Send back a success response, but don't include the password
            user.setPassword(null);
            resp.getWriter().write(gson.toJson(user));
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("Invalid credentials");
        }
    }
}
