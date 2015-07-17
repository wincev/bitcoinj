/*
 * Copyright 2014 Giannis Dzegoutanis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bitcoinj.params;

import org.bitcoinj.core.*;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class NetworksTests {
    class PeercoinDummy extends MainNetParams {
        PeercoinDummy() {
            super();
            family = "peercoin";
        }
    }
    class NubitsDummy extends MainNetParams {
        NubitsDummy() {
            super();
            family = "nubits";
            tokenId = 0x42;
        }
    }
    class NusharesDummy extends MainNetParams {
        NusharesDummy() {
            super();
            family = "nubits";
            tokenId = 0x53;
        }
    }
    class ReddcoinDummy extends MainNetParams {
        ReddcoinDummy() {
            super();
            family = "reddcoin";
        }
    }
    class BlackcoinDummy extends MainNetParams {
        BlackcoinDummy() {
            super();
            family = "peercoin";
        }
    }

    @Test
    public void networkFamily() throws Exception {
        assertEquals(Networks.Family.PEERCOIN, Networks.getFamily(new PeercoinDummy()));
        assertEquals(Networks.Family.PEERCOIN, Networks.getFamily(new BlackcoinDummy()));
        assertEquals(Networks.Family.NUBITS, Networks.getFamily(new NubitsDummy()));
        assertEquals(Networks.Family.NUBITS, Networks.getFamily(new NusharesDummy()));
        assertEquals(Networks.Family.BITCOIN, Networks.getFamily(MainNetParams.get()));
        assertEquals(Networks.Family.REDDCOIN, Networks.getFamily(new ReddcoinDummy()));
    }

    @Test
    public void networkFamilyTransactions() throws Exception {
        Transaction tx = makeTx(MainNetParams.get());
        assertTxEquals(tx, "01000000000100e1f505000000001976a914000000000000000000000000000000000000000088ac00000000");

        tx = makeTx(new PeercoinDummy());
        assertTxEquals(tx, "0100000099999999000100e1f505000000001976a914000000000000000000000000000000000000000088ac00000000");

        tx = makeTx(new NubitsDummy());
        assertTxEquals(tx, "0100000099999999000100e1f505000000001976a914000000000000000000000000000000000000000088ac0000000042");

        tx = makeTx(new NusharesDummy());
        assertTxEquals(tx, "0100000099999999000100e1f505000000001976a914000000000000000000000000000000000000000088ac0000000053");

        tx = makeTx(new ReddcoinDummy());
        assertTxEquals(tx, "01000000000100e1f505000000001976a914000000000000000000000000000000000000000088ac00000000");
        tx.setVersion(2);
        assertTxEquals(tx, "02000000000100e1f505000000001976a914000000000000000000000000000000000000000088ac0000000099999999");

        tx = makeTx(new BlackcoinDummy());
        assertTxEquals(tx, "0100000099999999000100e1f505000000001976a914000000000000000000000000000000000000000088ac00000000");;
    }

    private void assertTxEquals(Transaction tx, String expectedTx) {
        assertArrayEquals(Hex.decode(expectedTx), tx.bitcoinSerialize());
    }

    private Transaction makeTx(NetworkParameters parameters) throws AddressFormatException {
        byte[] hash160 = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        Address address = new Address(MainNetParams.get(), hash160);
        Transaction tx = new Transaction(parameters);
        tx.setTime(0x99999999);
        tx.addOutput(Coin.COIN, address);
        return tx;
    }
}
