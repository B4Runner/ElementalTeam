package my.group.here.elementalteam;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	public static final Logger log = Logger.getLogger("Minecraft");
	File folder = new File("plugins" + File.separator + "ElementalTeam");
	File players = new File(this.folder + File.separator + "playerdata.yml");
	FileConfiguration playerData = null;

	public void onEnable() {
		new EventListener(this);
		getServer().getPluginManager().registerEvents(this, this);
		log.info("ElementalTeam loaded");
		createFolder(this.folder);
		createFile(this.players);
		this.playerData = reloadCustomConfig(this.players);
	}

	public void onDisable() {
		log.info("ElemantalTeam Unloaded");
	}

	protected void createFile(File file) {
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void createFolder(File folder) {
		if (!folder.exists()) {
			folder.mkdir();
		}
	}

	public void saveCustomConfig(FileConfiguration config, File file) {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FileConfiguration reloadCustomConfig(File file) {
		createFolder(this.folder);
		createFile(file);

		FileConfiguration fileConfig = YamlConfiguration
				.loadConfiguration(file);

		return fileConfig;
	}

	@EventHandler
	public void onLogin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		this.playerData.addDefault("players." + player.getDisplayName()
				+ ".Team", "NoTeam");
		this.playerData.options().copyDefaults(true);
		saveCustomConfig(this.playerData, this.players);

		getConfig().addDefault("Kings.wind", "none");
		getConfig().addDefault("Kings.fire", "none");
		getConfig().addDefault("Kings.water", "none");
		getConfig().addDefault("Kings.earth", "none");
		getConfig().options().copyDefaults(true);
		saveConfig();

		joinTeam(event.getPlayer());
	}

	public Player findPlayer(String playerName) {
		Player[] gente = Bukkit.getServer().getOnlinePlayers();

		for (int i = 0; i < gente.length; i++) {
			if (playerName.equals(gente[i].getDisplayName())) {
				return gente[i];
			}
		}
		return null;
	}

	public void joinTeam(Player player) {
		if (this.playerData.getString(
				"players." + player.getDisplayName() + ".Team")
				.equalsIgnoreCase("EarthTeam")) {
			player.setHealthScale(24);
		} else {
			player.setHealthScale(20);
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("eteam")
				|| cmd.getName().equalsIgnoreCase("elementalteam")
				|| cmd.getName().equalsIgnoreCase("et")) {
			if (args[0].equals("join")) {

				String team = this.playerData.getString("players."
						+ ((Player) sender).getDisplayName() + ".Team");
				if (args[1].equalsIgnoreCase(team.substring(0,
						team.length() - 4).toLowerCase())) {
					sender.sendMessage("You are already in "
							+ args[1].substring(0, 1).toUpperCase()
							+ args[1].substring(1) + "Team");
					return true;
				}

				
				if (getConfig().getString("Kings.fire").equalsIgnoreCase(
						((Player) sender).getDisplayName())) {
					sender.sendMessage("You can't change faction if you are a King");
					return true;
				} else if (getConfig().getString("Kings.wind")
						.equalsIgnoreCase(((Player) sender).getDisplayName())) {
					sender.sendMessage("You can't change faction if you are a King");
					return true;
				} else if (getConfig().getString("Kings.water")
						.equalsIgnoreCase(((Player) sender).getDisplayName())) {
					sender.sendMessage("You can't change faction if you are a King");
					return true;
				} else if (getConfig().getString("Kings.earth")
						.equalsIgnoreCase(((Player) sender).getDisplayName())) {
					sender.sendMessage("You can't change faction if you are a King");
					return true;
				}

				
				if (args[1].equalsIgnoreCase("fire")) {
					this.playerData.set(
							"players." + ((Player) sender).getDisplayName()
									+ ".Team", "FireTeam");
					saveCustomConfig(this.playerData, this.players);

					joinTeam((Player) sender);
					sender.sendMessage("Welcome to FireTeam");
					return true;
				} else if (args[1].equalsIgnoreCase("wind")) {
					this.playerData.set(
							"players." + ((Player) sender).getDisplayName()
									+ ".Team", "WindTeam");
					saveCustomConfig(this.playerData, this.players);

					joinTeam((Player) sender);
					sender.sendMessage("Welcome to WindTeam");
					return true;
				} else if (args[1].equalsIgnoreCase("water")) {
					this.playerData.set(
							"players." + ((Player) sender).getDisplayName()
									+ ".Team", "WaterTeam");
					saveCustomConfig(this.playerData, this.players);

					joinTeam((Player) sender);
					sender.sendMessage("Welcome to WaterTeam");
					return true;
				} else if (args[1].equalsIgnoreCase("earth")) {
					this.playerData.set(
							"players." + ((Player) sender).getDisplayName()
									+ ".Team", "EarthTeam");
					saveCustomConfig(this.playerData, this.players);

					joinTeam((Player) sender);
					sender.sendMessage("Welcome to EarthTeam");
					return true;
				}

			} else if (args[0].equalsIgnoreCase("myteam")) {
				sender.sendMessage("You are in the "
						+ this.playerData.getString("players."
								+ ((Player) sender).getDisplayName() + ".Team"));
				return false;
			} else if (args[0].equalsIgnoreCase("help")) {
				sender.sendMessage("/et join [wind, fire, earth, water]");
				sender.sendMessage("/et myteam");
				sender.sendMessage("/et help");
				if (sender.isOp()) {
					sender.sendMessage("/et king <player> [wind, fire, earth, water]");
					sender.sendMessage("/et kickKing <player>");
				}

				return true;
			} else if (args[0].equalsIgnoreCase("king") && sender.isOp()
					&& findPlayer(args[1]) != null) {
				Player newKing = findPlayer(args[1]);
				Server server = newKing.getServer();
				if (args[2].equalsIgnoreCase("wind")
						|| args[2].equalsIgnoreCase("fire")
						|| args[2].equalsIgnoreCase("water")
						|| args[2].equalsIgnoreCase("earth")) {
					// king is king
					if (getConfig().getString("Kings." + args[2])
							.equalsIgnoreCase(args[1])) {
						sender.sendMessage("This player is already "
								+ args[2].toUpperCase() + " King");
						return true;
					}
					// king in right team
					String kingTeam = this.playerData.getString(
							"players." + newKing.getDisplayName() + ".Team")
							.toLowerCase();
					if (!(args[2].equalsIgnoreCase(kingTeam.substring(0,
							kingTeam.length() - 4)))) {
						sender.sendMessage("The new " + args[2].toUpperCase()
								+ " King must be in "
								+ args[2].substring(0, 1).toUpperCase()
								+ args[2].substring(1) + "Team");
						return true;
					}
					
					if (args[1].equalsIgnoreCase(getConfig().getString(
							"Kings.wind"))) {
						getConfig().set("Kings.wind", "none");
						saveConfig();
						kingTeam = "WIND";
					} else if (args[1].equalsIgnoreCase(getConfig().getString(
							"Kings.fire"))) {
						getConfig().set("Kings.fire", "none");
						saveConfig();
						kingTeam = "FIRE";
					} else if (args[1].equalsIgnoreCase(getConfig().getString(
							"Kings.water"))) {
						getConfig().set("Kings.water", "none");
						saveConfig();
						kingTeam = "WATER";
					} else if (args[1].equalsIgnoreCase(getConfig().getString(
							"Kings.earth"))) {
						getConfig().set("Kings.earth", "none");
						saveConfig();
						kingTeam = "EARTH";
					}
					if (newKing != null)
						newKing.sendMessage("You are no longer the " + kingTeam
								+ " King");

					// de-throne
					if (findPlayer(getConfig().getString("Kings." + args[2])) != null)
						findPlayer(getConfig().getString("Kings." + args[2]))
								.sendMessage(
										"You are no longer the "
												+ args[2].toUpperCase()
												+ " King");

					getConfig().set("Kings." + args[2],
							newKing.getDisplayName());
					saveConfig();

					server.broadcastMessage("The new " + args[2].toUpperCase()
							+ " King is " + newKing.getDisplayName());

					return true;
				} else {
					sender.sendMessage(args[2] + "team does not exist");
					return true;
				}
			} else if (args[0].equalsIgnoreCase("kickKing") && sender.isOp()
					&& findPlayer(args[1]) != null) {
				Player kicked = findPlayer(args[1]);
				
				String team = null;
				if (args[1].equalsIgnoreCase(getConfig()
						.getString("Kings.wind"))) {
					getConfig().set("Kings.wind", "none");
					saveConfig();
					team = "WIND";
				} else if (args[1].equalsIgnoreCase(getConfig().getString(
						"Kings.fire"))) {
					getConfig().set("Kings.fire", "none");
					saveConfig();
					team = "FIRE";
				} else if (args[1].equalsIgnoreCase(getConfig().getString(
						"Kings.water"))) {
					getConfig().set("Kings.water", "none");
					saveConfig();
					team = "WATER";
				} else if (args[1].equalsIgnoreCase(getConfig().getString(
						"Kings.earth"))) {
					getConfig().set("Kings.earth", "none");
					saveConfig();
					team = "EARTH";
				}
				if (team != null) {
					kicked.sendMessage("You are no longer the " + team
							+ " King");
					sender.sendMessage(args[1] + " is no longer the " + team
							+ " King");
				} else
					sender.sendMessage("King not found");

				return true;
			}
			return false;
		}
		return false;
	}
}
