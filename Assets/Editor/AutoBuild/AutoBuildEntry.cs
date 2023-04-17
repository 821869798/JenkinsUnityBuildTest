using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEditor;

namespace AutoBuild
{
    public static class AutoBuildEntry
    {
        [MenuItem("AutoBuild/BuildWindows")]
        static public void BuildWindowsMenu()
        {
            AutoBuildPlatformBase builder = new AutoBuildWindows();
            builder.SwitchPlatform();
            if (builder.ResetData())
            {
                builder.StartBuild();
            }
        }

        [MenuItem("AutoBuild/BuildAndroid")]
        static public void BuildAndroidMenu()
        {
            AutoBuildPlatformBase builder = new AutoBuildAndroid();
            builder.SwitchPlatform();
            if (builder.ResetData())
            {
                builder.StartBuild();
            }
        }

        [MenuItem("AutoBuild/BuildiOS")]
        static public void BuildiOSMenu()
        {
            AutoBuildPlatformBase builder = new AutoBuildiOS();
            builder.SwitchPlatform();
            if (builder.ResetData())
            {
                builder.StartBuild();
            }
        }


        static public void BuildWindows()
        {
            AutoBuildPlatformBase builder = new AutoBuildWindows();
            builder.SwitchPlatform();
            if (builder.ResetData() && builder.StartBuild())
            {

            }
            else
            {
                EditorApplication.Exit(1);
            }
        }

        static public void BuildAndroid()
        {
            AutoBuildPlatformBase builder = new AutoBuildAndroid();
            builder.SwitchPlatform();
            if (builder.ResetData() && builder.StartBuild())
            {

            }
            else
            {
                EditorApplication.Exit(1);
            }
        }

        static public void BuildiOS()
        {
            AutoBuildPlatformBase builder = new AutoBuildiOS();
            builder.SwitchPlatform();
            if (builder.ResetData() && builder.StartBuild())
            {

            }
            else
            {
                EditorApplication.Exit(1);
            }
        }
    }

}
