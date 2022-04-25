# Introduction

I use minecraft forge for playing minecraft, but i wanted to see what my right and left cps are, i searched for a good mod for minecraft 1.8, but i didn't found anything good. I only found 2 mods, one display only left clicks, and the other is too little for me and can't be zoomed... So as i'm a programmer, i decided to code a mod for myself, but i never really use java so sorry if the mod have problems, i'm just here for training and have a mod which respond my requirements.

And sorry for my bad english, i'm french ahah

# License

Do what you want with this mod, it's under MIT license, fork it, use it into a modpack, use it for your minecraft client, sell it without any modification (that's a joke, you aren't allowed to sell mods), i don't care about it, i just do this mod for me because i don't found any mod i want like this and because i like programing in my free time

# Build

for linux users:
    i'm on linux too, so i made sh files to make my life easier, i you want to run client for minecraft 1.8, you have to do `./launch_client.sh 1.8` same to run server and build the mod

for windows users:
    1. runClient: `gradlew --build-file build.gradle.1.8 runClient`
    2. runServer: `gradlew --build-file build.gradle.1.8 runClient`
    3. runBuild: `gradlew --build-file build.gradle.1.8 runClient`