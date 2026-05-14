package ru.denis.networkscanner;

import java.util.ArrayList;
import java.util.List;

public class CidrRange {
    private final String baseAddress;

    public CidrRange(String cidr) {
        String[] parts = cidr.split("/");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid CIDR format: " + cidr);
        }
        this.baseAddress = parts[0];
        int prefixLength = Integer.parseInt(parts[1]);

        if (prefixLength != 24) {
            throw new IllegalArgumentException("Only /24 is supported for now");
        }
    }

    public List<String> listHosts() {
        List<String> result = new ArrayList<>();

        String[] octets = baseAddress.split("\\.");
        String firstThreeOctets = octets[0]+"."+octets[1]+"."+octets[2]+".";
        for (int i = 1; i < 255; i++) {
            result.add(firstThreeOctets+i);
        }

        return result;
    }
}
