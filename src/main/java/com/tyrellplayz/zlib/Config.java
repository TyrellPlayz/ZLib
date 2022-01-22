package com.tyrellplayz.zlib;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config {

    public static class Client {

        public final ForgeConfigSpec.BooleanValue blockEntityInformation;

        Client(ForgeConfigSpec.Builder builder) {
            builder.comment("Client configuration settings").push("client");

            {
                builder.push("Debug");
                this.blockEntityInformation = builder.comment("Show block entity information in F3").translation(ZLib.MOD_ID+".config.client.debug.block_entity_information").define("blockEntityInformation",false);
                builder.pop();
            }
            builder.pop();
        }
    }

    public static class Server {

        Server(ForgeConfigSpec.Builder builder) {

        }

    }

    static final ForgeConfigSpec clientSpec;
    public static final Config.Client CLIENT;

    //static final ForgeConfigSpec serverSpec;
    //public static final Server SERVER;

    static {
        final Pair<Client, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(Config.Client::new);
        clientSpec = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();

        //final Pair<Server, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Server::new);
        //serverSpec = commonSpecPair.getRight();
        //SERVER = commonSpecPair.getLeft();
    }

}
