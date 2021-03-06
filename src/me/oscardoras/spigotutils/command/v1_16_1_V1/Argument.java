package me.oscardoras.spigotutils.command.v1_16_1_V1;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import me.oscardoras.spigotutils.io.TranslatableMessage;

/** Represents an argument for a Mojang Brigadier command. */
public abstract class Argument<T> {
	
	private final ArgumentType<?> rawType;
	private SuggestionsProvider suggestionsProvider = null;
	private Permission permission = null;
	
	/**
	 * Represents an argument for a Mojang Brigadier command.
	 * @param rawType the raw type for this argument
	 */
	protected Argument(ArgumentType<?> rawType) {
		this.rawType = rawType;
	}
	
	/**
	 * Gets the raw type for this argument.
	 * @return the raw type for this argument
	 */
	@SuppressWarnings("unchecked")
	protected final <R> ArgumentType<R> getRawType() {
		return (ArgumentType<R>) rawType;
	}
	
	/**
	 * Sets a suggestions provider for this argument.
	 * @param suggestionsProvider the suggestions provider to set
	 * @return this argument
	 */
	public Argument<T> withSuggestionsProvider(SuggestionsProvider suggestionsProvider) {
		this.suggestionsProvider = suggestionsProvider;
		return this;
	}
	
	/**
	 * Gets the suggestions provider for this argument.
	 * @return the suggestions provider for this argument
	 */
	public final SuggestionsProvider getSugesstionsProvider() {
		return suggestionsProvider;
	}
	
	/**
	 * Sets a permission for this argument.
	 * @param permission the permission to set
	 * @return this argument
	 */
	public final Argument<T> withPermission(Permission permission) {
		this.permission = permission;
		return this;
	}
	
	/**
	 * Gets the permission for this argument.
	 * @return the permission for this argument
	 */
	public final Permission getPermission() {
		return permission;
	}
	
	/**
	 * The argument parser.
	 * @param key the key of the command in the argument map
	 * @param context the command context of this command
	 * @throws CommandSyntaxException if the command is malformed
	 * @throws Exception if an exception occurs
	 */
	protected abstract T parse(String key, CommandContext<?> context) throws Exception;
	
	
	@FunctionalInterface
	public static interface SuggestionsProvider {
		/**
		 * The code to run to provides suggestions.
		 * @param cmd the data for the suggestion provider
		 * @return the suggestions
		 * @throws CommandSyntaxException if the command is malformed
		 * @throws Exception if another exception occurs
		 */
		Collection<String> run(SuggestedCommand cmd) throws Exception;
	}
	
	public static class SuggestedCommand {
		
		protected final Object source;
		protected final CommandSender sender;
		
		protected final CommandSender executor;
		protected final Location location;
		protected final Object[] args;
		
		protected SuggestedCommand(Object source, CommandSender sender, CommandSender executor, Location location, Object[] args) {
			this.source = source;
			this.sender = sender;
			
			this.executor = executor;
			this.location = location;
			this.args = args;
		}
		
		/**
		 * Gets the command executor.
		 * @return the command executor
		 */
		public CommandSender getExecutor() {
			return executor;
		}
		
		/**
		 * Gets the command location.
		 * @return the command location
		 */
		public Location getLocation() {
			return location;
		}
		
		/**
		 * Gets a command argument.
		 * @param index the index of the argument
		 * @return the command argument
		 */
		public Object getArg(int index) {
			return args[index];
		}
		
		/**
		 * Gets the sender language.
		 * @return the sender language
		 */
		public String getLanguage() {
			return TranslatableMessage.getLanguage(sender);
		}
		
		/**
		 * Checks if the sender has the permission.
		 * @param permission the permission to check
		 * @return true if the sender has the permission
		 */
		public boolean hasPermission(String permission) {
			return sender.hasPermission(permission);
		}
		
		/**
		 * Checks if the sender has the permission.
		 * @param permission the permission to check
		 * @return true if the sender has the permission
		 */
		public boolean hasPermission(Permission permission) {
			return sender.hasPermission(permission);
		}
		
	}
	
}