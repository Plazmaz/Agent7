package me.dylan.Agent7.http.Fuzzer;

import java.util.ArrayList;

import org.jsoup.Connection;

public interface IFuzzer {

	public abstract void beginInjection();

	public abstract void beginInjectionForms();

	public abstract void executeTestConnection(ArrayList<String> params);

	public abstract void sendGetPostPayloads(Connection connection, String payload);

	public abstract void gatherAllFormIds();

	public abstract void sendInitialRequest();
}
