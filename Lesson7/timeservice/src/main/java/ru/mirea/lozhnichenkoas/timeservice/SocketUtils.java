package ru.mirea.lozhnichenkoas.timeservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketUtils {
    // BufferedReader для получения входящих данных
    public static BufferedReader getReader(Socket socket) throws IOException {
        return (new BufferedReader(new InputStreamReader(socket.getInputStream())));
    }

    // Создает PrintWriter для отправки исходящих данных. Этот PrintWriter будет
    // автоматически сбрасывать поток при вызове println.
    // В примере не используется
    public static PrintWriter getWriter(Socket socket) throws IOException {
        return (new PrintWriter(socket.getOutputStream(), true));
    }
}
