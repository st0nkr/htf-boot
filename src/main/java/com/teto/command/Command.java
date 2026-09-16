package com.teto.command;

import com.teto.IOptional;

import java.util.Optional;
import java.util.function.Function;

public interface Command<X> extends Function<Context, Optional<X>>, IOptional {


}
