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

@WebServlet("/api/register")
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDTO newUser = gson.fromJson(req.getReader(), UserDTO.class);

        if (userDAO.userExists(newUser.getUsername())) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write("Username already exists");
            return;
        }

        if (userDAO.emailExists(newUser.getEmail())) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write("Email already exists");
            return;
        }

        boolean success = userDAO.insertUser(newUser);

        if (success) {
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(newUser));
        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
