<template>
  <span :class="`${prefixCls}- flex items-center `">
    <SvgIcon v-if="isSvgIcon" :name="getIcon?.replace('icon-', '')" :size="18" :class="`${prefixCls}-wrapper__icon mr-2`" />
    <Icon v-else-if="getIcon?.length" :icon="getIcon" :size="18" :class="`${prefixCls}-wrapper__icon mr-2`" />
    {{ getI18nName }}
  </span>
</template>
<script lang="ts">
  import { computed, defineComponent } from 'vue';

  import Icon from '/@/components/Icon/index';
  import SvgIcon from '/@/components/Icon/src/SvgIcon.vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { contentProps } from '../props';
  const { t } = useI18n();

  export default defineComponent({
    name: 'MenuItemContent',
    components: {
      Icon,
      SvgIcon,
    },
    props: contentProps,
    setup(props) {
      const { prefixCls } = useDesign('basic-menu-item-content');
      const getI18nName = computed(() => t(props.item?.name));
      const getIcon = computed(() => props.item?.icon ?? '');
      const isSvgIcon = computed(() => !!getIcon.value && getIcon.value.startsWith('icon-'));

      return {
        prefixCls,
        getI18nName,
        getIcon,
        isSvgIcon,
      };
    },
  });
</script>
