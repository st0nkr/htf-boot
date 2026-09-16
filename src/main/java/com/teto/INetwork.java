package com.teto;

import com.teto.command.Context;
import com.teto.domain.ipaddress.IPAddress;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public interface INetwork {
    default IPAddress myIPAddress(Context ctx) {
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            String ip = socket.getLocalAddress().getHostAddress();
            return new IPAddress(ip);
        } catch (UnknownHostException | SocketException e) {
            return null;
        }
    }
}
