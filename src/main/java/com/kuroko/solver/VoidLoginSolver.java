package com.kuroko.solver;

import com.kuroko.exception.AutoLoginFailedException;
import kotlin.coroutines.Continuation;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.utils.LoginSolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VoidLoginSolver extends LoginSolver {

    @Nullable
    @Override
    public Object onSolvePicCaptcha(@NotNull Bot bot, byte[] bytes, @NotNull Continuation<? super String> continuation) {
        throw new AutoLoginFailedException();
    }

    @Nullable
    @Override
    public Object onSolveSliderCaptcha(@NotNull Bot bot, @NotNull String s, @NotNull Continuation<? super String> continuation) {
        throw new AutoLoginFailedException();
    }

    @Nullable
    @Override
    public Object onSolveUnsafeDeviceLoginVerify(@NotNull Bot bot, @NotNull String s, @NotNull Continuation<? super String> continuation) {
        throw new AutoLoginFailedException();
    }
}
