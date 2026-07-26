English | [简体中文](README_CN.md)

# MaskSu

![](docs/images/logo.png)

A powerful root solution with Miuix UI and enhanced features.

## Features

- **MaskSU**: Provide root access for applications
- **Mask Modules**: Modify read-only partitions by installing modules
- **MaskBoot**: The most complete tool for unpacking and repacking Android boot images
- **Zygisk**: Run code in every Android applications' processes
- **Whitelist Mode**: Automatically adds all non-system apps to DenyList; only explicitly granted apps can access root. Bidirectional sync with Zygisk Next.
- **Module Repository**: Built-in browsing of the [KernelSU module repository](https://modules.kernelsu.org)
- **WebUI**: Supports module web interfaces with Monet color integration
- **Customization**: 6 theme modes, Monet color picking, dual home layouts (Classic / MaskSu), blur effects, floating bottom bar, page scaling, multi-icon support

## Downloads

- [GitHub Releases](https://github.com/Seyud/MaskSu/releases)
- [Telegram Channel](https://t.me/MaskSu)

## Useful Links

- [Installation Instruction](https://seyud.github.io/MaskSu/install.html)
- [Building and Development](https://seyud.github.io/MaskSu/build.html)
- [MaskSu Documentation](https://seyud.github.io/MaskSu/)
- [Zygisk module sample](https://github.com/topjohnwu/zygisk-module-sample)

## Bug Reports

**Only bug reports from Debug builds will be accepted.**

For installation issues, upload both boot image and install logs.<br>
For MaskSu issues, upload boot logcat or dmesg.<br>
For MaskSu app crashes, record and upload the logcat when the crash occurs.

Please report issues at [GitHub Issues](https://github.com/Seyud/MaskSu/issues).

## Translation Contributions

Default string resources for the MaskSu app and its stub APK are located here:

- `app/core/src/main/res/values/strings.xml`
- `app/stub-res/src/main/res/values/strings.xml`

Translate each and place them in the respective locations (`[module]/src/main/res/values-[lang]/strings.xml`).

## Acknowledgements

- [Magisk](https://github.com/topjohnwu/Magisk)
- [Miuix](https://github.com/compose-miuix-ui/miuix)
- [KernelSU](https://github.com/tiann/KernelSU)
- [InstallerX-Revived](https://github.com/wxxsfxyzm/InstallerX-Revived)

## License

    MaskSu, including all git submodules are free software:
    you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
