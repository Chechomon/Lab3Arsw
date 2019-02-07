/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;
import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;

/**
 *
 * @author Sergio
 */
public class Busqueda extends Thread {
    private int ini;
    private int fin;
    int checkedListsCount = 0;
    private String ipaddress;
    LinkedList<Integer> blackListOcurrences = new LinkedList<>();
    HostBlackListsValidator h;

    public Busqueda(int ini, int fin, String ipaddress, HostBlackListsValidator h) {
        this.ini = ini;
        this.fin = fin;
        this.ipaddress = ipaddress;
        this.h = h;
    }

    @Override
    public void run() {

        HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();
        for (int i = ini; i < fin; i++) {
            checkedListsCount++;
            if (skds.isInBlackListServer(i, ipaddress)) {
                h.agregarOcurrencia(i);
                if (h.getBlackListOcurrences().size() > 4) {
                    break;
                }

            }
        }
    }

    public LinkedList<Integer> retornarOcurrencias() {
        return blackListOcurrences;
    }

    public int getCheckedListsCount() {
        return checkedListsCount;
    }
}
